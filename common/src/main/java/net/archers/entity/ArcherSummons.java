package net.archers.entity;

import net.archers.content.ArcherSounds;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.spell_engine.api.datagen.SpellBuilder.Placements;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell.Impact.Action.Summon;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.api.spell.summon.SummonBehaviour;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.ArrayList;
import java.util.List;

/// Factory of the Archers summon definitions. Each builder returns a {@link Summon}
/// ({@code Spell.Impact.Action.Summon}) that a spell drops into a `SUMMON` impact — the engine's
/// {@code SpellImpacts} spawns and configures it.
public class ArcherSummons {

    public static Summon spiritWolf() {
        var b = new SummonBehaviour();
        b.lifespan.active_seconds = 30;
        b.lifespan.spawn_ticks = 20;
        b.lifespan.despawn_ticks = 20;

        // Movement: follow owner, teleport if too far
        b.movement.follow = new SummonBehaviour.Movement.Follow();
        b.movement.follow.teleport_after_distance = 32;
        b.movement.collision = SummonBehaviour.Movement.CollisionMode.ENEMIES;

        // Targeting: mirror owner's attacks and retaliate, auto-aggro hostiles
        b.targeting.attack_with_owner = true;
        b.targeting.revenge = true;
        b.targeting.automatic_targeting = SummonBehaviour.Targeting.AutoTarget.HOSTILE;

        // Single action: fast melee bites, no spells. `duration` matches the bite
        // animation's authored length (0.7s = 14 ticks) so the client plays it at 1x
        // speed instead of compressing it; cadence = max(duration, 20 / speed) ≈ 14
        // ticks, still ~40% faster than the Frost Elemental's 20-tick swings.
        var attack = new SummonBehaviour.Action.MeleeAttack();
        attack.max_range = 0; // no engagement cap — chase any acquired target (default 3 blocks no-chase)
        attack.speed = 1.5F;
        attack.duration = 14;
        attack.windup = 0.5F;
        attack.movement_speed = 1.2F;
        // Sounds at 0.75 volume (id, volume, pitch, randomness); the growl stays at 0.5 — full
        // volume was too loud.
        attack.swing_sound = new Sound("minecraft:entity.wolf.growl", 0.5F, 1F, 0.1F);
        attack.impact_sound = new Sound("minecraft:entity.player.attack.strong", 0.75F, 1F, 0.1F);
        b.actions = List.of(SummonBehaviour.Action.attack(attack));

        // Lifecycle sounds: vanilla wolf set
        b.sounds.spawn = Sound.withVolume(ArcherSounds.SPIRIT_WOLF_SPAWN.id(), 0.75F);
        b.sounds.despawn = new Sound("minecraft:entity.wolf.whine", 0.75F, 1F, 0.1F);
        b.sounds.hurt = new Sound("minecraft:entity.wolf.hurt", 0.75F, 1F, 0.1F);
        b.sounds.death = new Sound("minecraft:entity.wolf.death", 0.75F, 1F, 0.1F);
        b.sounds.ambient = new Sound("minecraft:entity.wolf.pant", 0.75F, 1F, 0.1F);
        b.sounds.step = new Sound("minecraft:entity.wolf.step", 0.75F, 1F, 0.1F);

        // Spawn FX: a burst of spectral sparks rushing outward from a pipe around the spawn point,
        // wrapped in a sphere of the same sparks as the spirit takes form
        b.spawn_fx = Fx.Visuals.of(
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(b2 -> b2.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .count(20).speed(0.15F, 0.35F)
                                .verticalOrigin(Batches.FEET).extent(0.5F)),
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(b2 -> b2.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.15F, 0.35F).extent(0.5F))
        );

        // Despawn FX: the spirit dissolves upward — a rising pillar of ascending spectral sparks.
        b.despawn_fx = Fx.Visuals.of(
                natureSpark(ParticleGroup.Motion.ASCEND)
                        .batch(b2 -> b2.shape(ParticleGroup.Shape.PILLAR)
                                .count(20).speed(0.15F, 0.35F)
                                .verticalOrigin(Batches.FEET).extent(0.5F))
        );

        // Placement: a tight ring around the caster — front, right, left, back, each 1 block out,
        // ground-snapped and facing the caster's yaw, staggered 5 ticks apart (mirrors the Fire
        // Hydra formation). With spawn_count = 2 the loop fills the first two slots: front, right.
        float d = 1F;
        var placements = List.of(
                Placements.pointAtAngle(d, 0F, 0),    // front
                Placements.pointAtAngle(d, 90F, 5),   // right
                Placements.pointAtAngle(d, 270F, 10), // left
                Placements.pointAtAngle(d, 180F, 15)  // back
        );

        var summon = new Summon(ArcherEntities.SPIRIT_WOLF.id.toString(), b, placements, 2);
        summon.attribute_scaling.entries = rangedCombatScaling();
        return summon;
    }

    // MARK: FX helpers

    /// A nature-tinted spark, motion picked per use — the spirit-wolf palette.
    private static ParticleGroupBuilder natureSpark(ParticleGroup.Motion motion) {
        return ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, motion, Color.NATURE);
    }

    // MARK: Scaling helpers

    /// Per-wolf owner-scaled combat stat block, scaling off the owner's ranged damage attribute
    /// (physical ranged school): health, armor, attack damage, attack knockback and knockback
    /// resistance. Health and attack-damage coefficients are tuned down from a lone-summon block
    /// (health 2.0 → 1.0, damage 0.5 → 0.3) so the two-wolf pack lands near a single wolf's
    /// former aggregate rather than doubling it.
    private static List<AttributeScaling.Entry> rangedCombatScaling() {
        var s = ExternalSpellSchools.PHYSICAL_RANGED.attributeEntry.getRegisteredName();
        var entries = new ArrayList<AttributeScaling.Entry>();
        entries.add(scalingEntry(Attributes.MAX_HEALTH.getRegisteredName(), s, 0, 1.0));
        entries.add(scalingEntry(Attributes.ARMOR.getRegisteredName(), s, 10, 0.1));
        entries.add(scalingEntry(Attributes.ATTACK_DAMAGE.getRegisteredName(), s, 0, 0.3));
        entries.add(scalingEntry(Attributes.ATTACK_KNOCKBACK.getRegisteredName(), s, 0, 0.1));
        entries.add(scalingEntry(Attributes.KNOCKBACK_RESISTANCE.getRegisteredName(), s, 5, 0.05));
        return entries;
    }

    /// A single attribute-scaling entry: `targetAttribute += base + ownerAttribute * coefficient`
    /// (ADD_VALUE).
    private static AttributeScaling.Entry scalingEntry(String targetAttribute, String ownerAttribute,
                                                       double base, double coefficient) {
        var entry = new AttributeScaling.Entry();
        entry.attribute_id = targetAttribute;
        entry.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                ownerAttribute, AttributeModifier.Operation.ADD_VALUE, base, coefficient));
        return entry;
    }
}
