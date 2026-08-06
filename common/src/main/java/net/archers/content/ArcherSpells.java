package net.archers.content;

import net.archers.ArchersMod;
import net.archers.effect.ArcherEffects;
import net.archers.entity.ArcherSummons;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArcherSpells {
    public enum Book {
        ARCHER("Archery Manual", "Archery Scroll",
                "Spell Book of Archers, using ranged weapons and archery skills to defeat enemies from afar\n- Strengths: Dealing high damage at range and maintaining mobility\n- Weaknesses: Heavily armored enemies\n- Equipment: Moderately armored");

        /** Display name of the generated spell book item. Source for {@code item.archers.spell_book/<book>}. */
        public final String bookName;
        /** Display name of the generated spell scroll item. Source for {@code item.archers.spell_scroll/<book>}. */
        public final String scrollName;
        /** Spell binding tooltip. Source for {@code item.archers.spell_book/<book>.spell_binding.description}. */
        public final String bindingDescription;
        Book(String bookName, String scrollName, String bindingDescription) {
            this.bookName = bookName;
            this.scrollName = scrollName;
            this.bindingDescription = bindingDescription;
        }
    }
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable List<Object> weaponGroups,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, List.of(), null);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, weaponGroups, book);
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    // MARK: Spell groups
    // Prefixed by the book they belong to, since a group is only ever read alongside the groups of
    // other mods, where a bare `nature` would say nothing about whose nature it is.

    public static final String MARKSMAN = "archer_marksman";
    public static final String NATURE = "archer_nature";

    // MARK: Shared particle effects
    // Motion is chosen per effect rather than baked into the particle id, so the one registered
    // `magic_spark` texture covers every spectral-spark use below.

    /// A nature-tinted spark, motion picked per use — the Magic Arrow / spirit palette.
    private static ParticleGroupBuilder natureSpark(ParticleGroup.Motion motion) {
        return ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, motion, Color.NATURE);
    }


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
        var name = "Power Shot";
        var description = "Your next {effect_amplifier_cap} arrows inflict the target with Hunter's Mark for {effect_duration} seconds, increasing its damage taken by "
                + TooltipTokens.effect(ArcherEffects.HUNTERS_MARK.id)
                + ", stacking up to {effect_amplifier_cap} times.";
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.tier = 2;
        spell.group = MARKSMAN;


        spell.release.sound = Sound.withVolume(ArcherSounds.MARKER_SHOT.id(), 0.5F);
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.FLOAT, Color.RAGE)
                        .batch(Batches.casting(10, 0.1F).andThen(b -> b.speed(0.01F, 0.1F)))
        );

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
        debuff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(3).speed(0.01F, 0.1F))
        );
        spell.impacts = List.of(debuff);
        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.bypass_iframes = true;

        configureCooldown(spell, 8);
        return new Entry(id, spell, name, description);
    }

    public static final Entry entangling_roots = add(entangling_roots().book(Book.ARCHER));
    private static Entry entangling_roots() {
        var id = Identifier.of(ArchersMod.ID, "entangling_roots");
        var name = "Entangling Roots";
        var description = "Conjures roots to sprout from the ground in the nearby area, slowing down enemies for {cloud_duration} seconds.";
        var spell = activeSpellBase();
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.tier = 2;
        spell.group = NATURE;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 3.5F;
        cloud.volume.area.vertical_range_multiplier = 0.3F;
        cloud.volume.sound = new Sound(ArcherSounds.ENTANGLING_ROOTS.id());
        cloud.impact_tick_interval = 15;
        cloud.time_to_live_seconds = 8;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.roots)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(2)
                                .verticalOrigin(Batches.FEET))
        );
        spell.deliver.clouds = List.of(cloud);

        var debuff = createEffectImpact(ArcherEffects.ENTANGLING_ROOTS.id, 1);
        debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        debuff.action.status_effect.apply_limit = new Spell.Impact.Action.StatusEffect.ApplyLimit();
        debuff.action.status_effect.apply_limit.health_base = 50;
        debuff.action.status_effect.apply_limit.spell_power_multiplier = 5;
        debuff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("falling_spore_blossom")
                        .batch(Batches.impact(2, 0.2F).andThen(b -> b.speed(0.1F, 0.2F)))
        );
        spell.impacts = List.of(debuff);

        configureCooldown(spell, 18);
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, name, description);
    }

    public static final Entry barrage = add(barrage().book(Book.ARCHER));
    private static Entry barrage() {
        var id = Identifier.of(ArchersMod.ID, "barrage");
        var name = "Barrage";
        var description = "Fires multiple arrows in quick succession.";
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.tier = 3;
        spell.group = MARKSMAN;

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

        return new Entry(id, spell, name, description);
    }

    public static final Entry rain_of_arrows = add(rain_of_arrows().book(Book.ARCHER));
    private static Entry rain_of_arrows() {
        var id = Identifier.of(ArchersMod.ID, "rain_of_arrows");
        var name = "Rain of Arrows";
        var description = "Rains arrows over the targeted area for 5 seconds, each dealing {damage} damage to enemies within {impact_range} blocks.";
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 32;
        spell.tier = 4;
        spell.group = MARKSMAN;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_upwards_pull");
        spell.active.cast.animates_ranged_weapon = true;
        spell.active.cast.sound = new Sound(ArcherSounds.BOW_PULL.id());

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_upwards_release");
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL).count(4).speed(0.6F, 0.9F))
        );
        spell.release.sound = new Sound(ArcherSounds.RAIN_OF_ARROWS_RELEASE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = true;

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        spell.deliver.meteor = new Spell.Delivery.Meteor();
        spell.deliver.meteor.launch_height = 10;
        spell.deliver.meteor.launch_radius = 2;
        spell.deliver.meteor.launch_properties.velocity = 1.5F;
        spell.deliver.meteor.launch_properties.extra_launch_count = 20;
        spell.deliver.meteor.launch_properties.extra_launch_delay = 5;

        var projectile = new Spell.ProjectileData();
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("crit")
                        .batch(Batches.travel(1, 0.05F).andThen(b -> b.speed(0, 0.05F))),
                // Streams along the travel direction (LOOK-aligned LINE): forward while
                // launched, downward once falling
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(0.25F).speed(0.1F, 0.2F)
                                .alignment(ParticleGroup.Alignment.LOOK))
        );
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("archers:spell_projectile/volley_arrow", 1F, LightEmission.GLOW);
        spell.deliver.meteor.projectile = projectile;

        var damage = damage(0.4F, 0.25F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("crit")
                        .batch(Batches.impact(8, 0.4F))
        );
        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        var impactRadius = 1.5F;
        spell.area_impact.radius = impactRadius;
        spell.area_impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .appearance(a -> a.scale(impactRadius).color(Color.from(0xceb15c).toRGBA())),
                ParticleGroupBuilder.of("crit")
                        .batch(Batches.impact(20, 0.5F).andThen(b -> b.speed(0.25F, 0.5F))),
                ParticleGroupBuilder.of("poof")
                        .batch(Batches.impact(5, 0.1F).andThen(b -> b.speed(0.02F, 0.1F)))
        );
        spell.area_impact.sound = new Sound(ArcherSounds.RAIN_OF_ARROWS_IMPACT.id());

        configureCooldown(spell, 15);
        return new Entry(id, spell, name, description);
    }

    public static final Entry magic_arrow = add(magic_arrow().book(Book.ARCHER));
    private static Entry magic_arrow() {
        var id = Identifier.of(ArchersMod.ID, "magic_arrow");
        var name = "Magic Arrow";
        var description = "Shoots a magical arrow piercing thru all enemies in its path, dealing {damage} damage to each target. The longer the cast is held, the harder it hits and the further the arrow flies.";
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        // Base range 32, plus up to +32 from the charge bonus below -> the same 64 total at full charge.
        spell.range = 16;
        spell.tier = 4;
        spell.group = NATURE;

        // Charged cast: hold to charge. CHARGE scales the innate output (damage) by default; the charge
        // bonus additionally extends how far the arrow flies, scaled by the curved release ratio.
        var charge = SpellBuilder.Casting.charge(spell, 1F);
        charge.bonus.range_add = 48F;

        spell.active.cast.animation = PlayerAnimation.of("spell_engine:archery_pull");
        spell.active.cast.animates_ranged_weapon = true;
        spell.active.cast.particles = List.of(
                natureSpark(ParticleGroup.Motion.FLOAT)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(4).speed(0.02F, 0.1F)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .invert(true).preTravel(14))
        );
        spell.active.cast.sound = new Sound(SpellEngineSounds.GENERIC_WIND_CHARGING.id());

        spell.release.animation = PlayerAnimation.of("spell_engine:archery_release");
        spell.release.sound = new Sound(ArcherSounds.MAGIC_ARROW_RELEASE.id());
        spell.release.visuals = Fx.Visuals.of(
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(50).speed(0.18F, 0.2F)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .alignment(ParticleGroup.Alignment.LOOK)),
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(25).speed(0.28F, 0.3F)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .alignment(ParticleGroup.Alignment.LOOK))
        );

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        var shoot = new Spell.Delivery.ShootProjectile();
        shoot.launch_properties.velocity = 1.5F;
        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 2;
        projectile.perks.pierce = 99999;
        projectile.client_data = new Spell.ProjectileData.Client();
        // Double helix around the flight path: two LINE strands 180 degrees apart, swept around the
        // travel axis by the roll rate every tick. `Batches.helix` is this pattern; the speeds are
        // spelled out because this wake is tighter than the preset's default spread.
        projectile.client_data.travel_particles = List.of(
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(Batches.helix(5, 0.15F, 18, 0).andThen(b -> b.speed(0.14F, 0.15F))),
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(Batches.helix(5, 0.15F, 18, 180).andThen(b -> b.speed(0.14F, 0.15F))),
                ParticleGroupBuilder.of("firework")
                        .batch(Batches.travel(2, 0.05F).andThen(b -> b.speed(0, 0.05F))),
                natureSpark(ParticleGroup.Motion.DECELERATE)
                        .batch(Batches.travel(10, 0.05F).andThen(b -> b.speed(0, 0.05F)))
        );
        projectile.client_data.light_level = 10;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("archers:spell_projectile/magic_arrow", 1.2F, LightEmission.RADIATE);
        shoot.projectile = projectile;
        spell.deliver.projectile = shoot;

        var damage = damage(1.2F, 2);
        damage.visuals = Fx.Visuals.of(
                natureSpark(ParticleGroup.Motion.BURST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(1.2F, 1.8F))
        );
        damage.sound = new Sound(ArcherSounds.MAGIC_ARROW_IMPACT.id());

        spell.impacts = List.of(damage);

        configureCooldown(spell, 8);
        configureArrowCost(spell);

        return new Entry(id, spell, name, description);
    }

    public static final Entry spirit_wolf = add(spirit_wolf().book(Book.ARCHER));
    private static Entry spirit_wolf() {
        var id = Identifier.of(ArchersMod.ID, "spirit_wolf");
        var name = "Spirit Wolf Pack";
        var description = "Summons a pack of " + TooltipTokens.placeholder(TooltipTokens.summonCountToken) + " Spirit Wolves to fight by your side for " + TooltipTokens.placeholder(TooltipTokens.summonDurationToken) + " sec, empowered by your Ranged Damage.";
        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 16;
        spell.tier = 3;
        spell.group = NATURE;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(ArcherSounds.SPIRIT_WOLF_SUMMON.id());

        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = Spell.Impact.Action.Type.SUMMON;
        impact.action.summon = ArcherSummons.spiritWolf();
        spell.impacts = List.of(impact);

        configureCooldown(spell, 45);
        spell.cost.cooldown.haste_affected = false; // summon uptime shouldn't scale with haste
        return new Entry(id, spell, name, description);
    }
}
