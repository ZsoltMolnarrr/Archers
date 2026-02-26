package net.archers.content;

import net.archers.ArchersMod;
import net.archers.effect.ArcherEffects;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArcherSpells {
    public enum Book { ARCHER }
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator,
                        @Nullable List<Object> weaponGroups,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, null, List.of(), null);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, mutator, weaponGroups, book);
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final String PRIMARY_GROUP = "primary";

    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();

        spell.learn = new Spell.Learn();

        return spell;
    }

    private static Spell.Impact createEffectImpact(Identifier effectId, float duration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectId.toString();
        buff.action.status_effect.duration = duration;
        return buff;
    }

    private static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = duration;
    }

    private static void configureArrowCost(Spell spell) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.item = new Spell.Cost.Item();
        spell.cost.item.id = "arrow";
    }

    private static Spell.Impact damage(float coefficient, float knockback) {
        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = Spell.Impact.Action.Type.DAMAGE;
        impact.action.damage = new Spell.Impact.Action.Damage();
        impact.action.damage.spell_power_coefficient = coefficient;
        impact.action.damage.knockback = knockback;
        return impact;
    }

    public static final Entry power_shot = add(power_shot().book(Book.ARCHER));
    private static Entry power_shot() {
        var id = Identifier.of(ArchersMod.ID, "power_shot");
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.tier = 2;


        spell.release.sound = Sound.withVolume(ArcherSounds.MARKER_SHOT.id(), 0.5F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.STRIPE,
                        SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        10, 0.01F, 0.1F)
                        .color(Color.RAGE.toRGBA())
        };

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        var stash = new Spell.Delivery.StashEffect();
        stash.id = ArcherEffects.HUNTERS_MARK_STASH.id.toString();
        stash.duration = 12;
        stash.amplifier = 2;
        var shootTrigger = new Spell.Trigger();
        shootTrigger.type = Spell.Trigger.Type.ARROW_SHOT;
        stash.triggers = List.of(shootTrigger);
        stash.impact_mode = Spell.Delivery.StashEffect.ImpactMode.TRANSFER;
        spell.deliver.stash_effect = stash;

        var debuff = createEffectImpact(ArcherEffects.HUNTERS_MARK.id, 12);
        debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
        debuff.action.status_effect.amplifier = 1;
        debuff.action.status_effect.amplifier_cap = 2;
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch("firework",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.CENTER,
                        3, 0.01F, 0.1F)
        };
        spell.impacts = List.of(debuff);
        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.bypass_iframes = true;

        configureCooldown(spell, 8);
        return new Entry(id, spell, "Power Shot", "");
    }

    public static final Entry entangling_roots = add(entangling_roots().book(Book.ARCHER));
    private static Entry entangling_roots() {
        var id = Identifier.of(ArchersMod.ID, "entangling_roots");
        var spell = activeSpellBase();
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.tier = 3;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 3.5F;
        cloud.volume.area.vertical_range_multiplier = 0.3F;
        cloud.volume.sound = new Sound(ArcherSounds.ENTANGLING_ROOTS.id());
        cloud.impact_tick_interval = 15;
        cloud.time_to_live_seconds = 8;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.roots.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        2, 0, 0)
        };
        spell.deliver.clouds = List.of(cloud);

        var debuff = createEffectImpact(ArcherEffects.ENTANGLING_ROOTS.id, 1);
        debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        debuff.action.status_effect.apply_limit = new Spell.Impact.Action.StatusEffect.ApplyLimit();
        debuff.action.status_effect.apply_limit.health_base = 50;
        debuff.action.status_effect.apply_limit.spell_power_multiplier = 5;
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch("falling_spore_blossom",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        2, 0.1F, 0.2F)
        };
        spell.impacts = List.of(debuff);

        configureCooldown(spell, 18);
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, "Entangling Roots", "");
    }

    public static final Entry barrage = add(barrage().book(Book.ARCHER));
    private static Entry barrage() {
        var id = Identifier.of(ArchersMod.ID, "barrage");
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.tier = 3;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_pull");
        spell.active.cast.animates_ranged_weapon = true;
        spell.active.cast.sound = new Sound(ArcherSounds.BOW_PULL.id());

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_release");

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.SHOOT_ARROW;
        spell.deliver.shoot_arrow = new Spell.Delivery.ShootArrow();
        spell.deliver.shoot_arrow.launch_properties.velocity = 3.15F;
        spell.deliver.shoot_arrow.launch_properties.extra_launch_count = 2;

        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.damage_multiplier = 0.75F;
        spell.arrow_perks.bypass_iframes = true;
        spell.arrow_perks.knockback = 0.5F;

        configureCooldown(spell ,10);
        configureArrowCost(spell);
        spell.cost.item.consume = false;

        return new Entry(id, spell, "Barrage", "");
    }

    public static final Entry magic_arrow = add(magic_arrow().book(Book.ARCHER));
    private static Entry magic_arrow() {
        var id = Identifier.of(ArchersMod.ID, "magic_arrow");
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 64;
        spell.tier = 4;

        spell.active.cast.duration = 1F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_pull");
        spell.active.cast.animates_ranged_weapon = true;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        4, 0.02F, 0.1F)
                        .invert()
                        .preSpawnTravel(14)
                        .color(Color.NATURE.toRGBA())
        };
        spell.active.cast.sound = new Sound(SpellEngineSounds.GENERIC_WIND_CHARGING.id());

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_release");
        spell.release.sound = new Sound(ArcherSounds.MAGIC_ARROW_RELEASE.id());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 50,0.18F,0.2F, 0)
                        .color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 25,0.28F,0.3F, 0)
                        .color(Color.NATURE.toRGBA())
        };

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        var shoot = new Spell.Delivery.ShootProjectile();
        shoot.launch_properties.velocity = 1.5F;
        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 2;
        projectile.perks.pierce = 99999;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 5,0.14F,0.15F, 0)
                        .roll(18).color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 5,0.14F,0.15F, 0).rollOffset(180)
                        .roll(18).color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 2,0F,0.05F, 0),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 10,0F,0.05F, 0)
                        .color(Color.NATURE.toRGBA())
        };
        projectile.client_data.light_level = 10;
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "archers:spell_projectile/magic_arrow";
        projectile.client_data.model.light_emission = LightEmission.RADIATE;
        projectile.client_data.model.scale = 1.2F;
        shoot.projectile = projectile;
        spell.deliver.projectile = shoot;

        var damage = damage(1.2F, 2);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 1.2F, 1.8F)
                        .color(Color.NATURE.toRGBA())
        };
        damage.sound = new Sound(ArcherSounds.MAGIC_ARROW_IMPACT.id());

        spell.impacts = List.of(damage);

        configureCooldown(spell, 8);
        configureArrowCost(spell);

        return new Entry(id, spell, "Magic Arrow", "");
    }
}
