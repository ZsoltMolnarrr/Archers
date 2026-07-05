package net.archers.entity;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.spell_engine.api.datagen.SpellBuilder.Placements;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell.Impact.Action.Summon;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.VFX;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.api.spell.summon.SummonBehaviour;

import java.util.ArrayList;
import java.util.List;

/// Factory of the Archers summon definitions. Each builder returns a {@link Summon}
/// ({@code Spell.Impact.Action.Summon}) that a spell drops into a `SUMMON` impact — the engine's
/// {@code SpellHelper} spawns and configures it.
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
        attack.speed = 1.5F;
        attack.duration = 14;
        attack.windup = 0.5F;
        attack.movement_speed = 1.2F;
        attack.swing_sound = "minecraft:entity.wolf.growl";
        attack.impact_sound = "minecraft:entity.player.attack.strong";
        b.actions = List.of(SummonBehaviour.Action.attack(attack));

        // Lifecycle sounds: vanilla wolf set
        b.sounds.spawn = "minecraft:entity.wolf.howl";
        b.sounds.despawn = "minecraft:entity.wolf.whine";
        b.sounds.hurt = "minecraft:entity.wolf.hurt";
        b.sounds.death = "minecraft:entity.wolf.death";
        b.sounds.ambient = "minecraft:entity.wolf.pant";
        b.sounds.step = "minecraft:entity.wolf.step";

        // Spawn FX: a column of soul wisps as the spirit takes form
        b.spawn_fx = new VFX();
        b.spawn_fx.particles = new ParticleBatch[] {
                new ParticleBatch("soul",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        40, 0.05F, 0.3F)
                        .extent(0.5F)
        };

        // Placement: 2 blocks ahead of the caster, snapped to the ground, keeping its own facing.
        var placement = Placements.point(2F, 0F, 0);
        placement.apply_yaw = false;

        var summon = new Summon(SpiritWolfEntity.ID.toString(), b, List.of(placement), 1);
        summon.attribute_scaling.entries = rangedCombatScaling();
        return summon;
    }

    // MARK: Scaling helpers

    /// The standard owner-scaled combat stat block, scaling off the owner's ranged damage
    /// attribute (physical ranged school): health, armor, attack damage, attack knockback
    /// and knockback resistance.
    private static List<AttributeScaling.Entry> rangedCombatScaling() {
        var s = ExternalSpellSchools.PHYSICAL_RANGED.attributeEntry.getIdAsString();
        var entries = new ArrayList<AttributeScaling.Entry>();
        entries.add(scalingEntry(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), s, 0, 2.0));
        entries.add(scalingEntry(EntityAttributes.GENERIC_ARMOR.getIdAsString(), s, 10, 0.1));
        entries.add(scalingEntry(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), s, 0, 0.5));
        entries.add(scalingEntry(EntityAttributes.GENERIC_ATTACK_KNOCKBACK.getIdAsString(), s, 0, 0.1));
        entries.add(scalingEntry(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getIdAsString(), s, 5, 0.05));
        return entries;
    }

    /// A single attribute-scaling entry: `targetAttribute += base + ownerAttribute * coefficient`
    /// (ADD_VALUE).
    private static AttributeScaling.Entry scalingEntry(String targetAttribute, String ownerAttribute,
                                                       double base, double coefficient) {
        var entry = new AttributeScaling.Entry();
        entry.attribute_id = targetAttribute;
        entry.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                ownerAttribute, EntityAttributeModifier.Operation.ADD_VALUE, base, coefficient));
        return entry;
    }
}
