package net.archers.entity;

import net.archers.ArchersMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.summon.SummonedEntities;
import net.spell_engine.api.spell.summon.SummonedEntityConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArcherEntities {

    /// Pairs a custom entity's type with its display name (for lang datagen) and, optionally, its
    /// summoned-entity attribute defaults. Mirrors the {@code Effects.Entry} pattern: the name lives
    /// next to the registration so it can't drift or be forgotten.
    public static class Entry<T extends Entity> {
        public final Identifier id;
        /// English display name, emitted as {@code entity.<namespace>.<path>} by lang datagen.
        public final String name;
        public final EntityType<T> type;
        /// Attribute defaults for summoned entities (seeded into config/spell_engine/summoned_entities.json).
        /// Null for entities that aren't spell-power-scaled summons.
        @Nullable public final SummonedEntityConfig.Entry summonConfig;

        public Entry(Identifier id, String name, EntityType<T> type) {
            this(id, name, type, null);
        }
        public Entry(Identifier id, String name, EntityType<T> type, @Nullable SummonedEntityConfig.Entry summonConfig) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.summonConfig = summonConfig;
        }
    }

    public static final List<Entry<?>> entries = new ArrayList<>();
    private static <T extends Entity> Entry<T> add(Entry<T> entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry<SpiritWolfEntity> SPIRIT_WOLF = add(new Entry<>(
            Identifier.of(ArchersMod.ID, "spirit_wolf"),
            "Spirit Wolf",
            EntityType.Builder.<SpiritWolfEntity>create(SpiritWolfEntity::new, SpawnGroup.MISC)
                    // dimensions(float, float) yields `changing` (fixed=false) so
                    // EntityDimensions.scaled() applies GENERIC_SCALE if one is ever added
                    // Halfway between the vanilla wolf (0.6 x 0.85) and the original 1x1
                    .dimensions(0.8F, 0.925F)
                    .maxTrackingRange(64)
                    .trackingTickInterval(3)
                    .build(),
            spiritWolfDefaults()));

    // Default base attributes per summon — seeded into the central SpellEngine config
    // (config/spell_engine/summoned_entities.json) via SummonedEntities.registerAttributes.

    public static SummonedEntityConfig.Entry spiritWolfDefaults() {
        var e = new SummonedEntityConfig.Entry();
        e.common = new SummonedEntityConfig.CommonAttributes(20, 0.35, 3);
        e.common.follow_range = 32;
        // Flat +50% over the vanilla base values (jump 0.42, step 0.6) — agile like a wolf
        e.custom.add(new SummonedEntityConfig.CustomAttribute(
                EntityAttributes.GENERIC_JUMP_STRENGTH.getIdAsString(), 0.63));
        e.custom.add(new SummonedEntityConfig.CustomAttribute(
                EntityAttributes.GENERIC_STEP_HEIGHT.getIdAsString(), 0.9));
        return e;
    }

    public static void register() {
        for (var entry : entries) {
            Registry.register(Registries.ENTITY_TYPE, entry.id, entry.type);
            if (entry.summonConfig != null) {
                // Only summoned (living) entities carry a config; safe by construction.
                @SuppressWarnings("unchecked")
                var livingType = (EntityType<? extends LivingEntity>) entry.type;
                SummonedEntities.registerAttributes(entry.id, livingType, entry.summonConfig);
            }
        }
    }
}
