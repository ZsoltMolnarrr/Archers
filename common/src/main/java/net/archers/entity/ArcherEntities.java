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
import net.tiny_config.ConfigManager;
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
        /// Attribute defaults for summoned entities (seeded into Archers' own config/archers/summoned_entities.json).
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
                    // Vanilla build(String id) — the no-arg build() is a Fabric API interface-injected
                    // default (FabricEntityType.Builder) absent on NeoForge at runtime.
                    .build("spirit_wolf"),
            spiritWolfDefaults()));

    // Default base attributes per summon — seeded into Archers' OWN config file
    // (config/archers/summoned_entities.json), versioned independently. The live values are read back
    // through summonConfig at registration time.

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

    /// Archers' own summoned-entity config file, seeded from the per-entity defaults above and versioned
    /// independently (bump `schemaVersion` to reset users' files after a defaults change). Declared after
    /// the entity constants so {@link #entries} is fully populated when the defaults are collected.
    public static final ConfigManager<SummonedEntityConfig> summonConfig = new ConfigManager<>
            ("summoned_entities", seededDefaults())
            .builder()
            .setDirectory(ArchersMod.ID)
            .schemaVersion(1)
            .sanitize(true)
            .build();

    private static SummonedEntityConfig seededDefaults() {
        var config = new SummonedEntityConfig();
        for (var entry : entries) {
            if (entry.summonConfig != null) {
                config.entries.put(entry.id.toString(), entry.summonConfig);
            }
        }
        return config;
    }

    public static void register() {
        summonConfig.refresh(); // load (or write) Archers' own config file before reading values from it
        for (var entry : entries) {
            Registry.register(Registries.ENTITY_TYPE, entry.id, entry.type);
            if (entry.summonConfig != null) {
                // Only summoned (living) entities carry a config; safe by construction.
                @SuppressWarnings("unchecked")
                var livingType = (EntityType<? extends LivingEntity>) entry.type;
                // Inject Archers' config as the attribute source — a plain Function<Identifier, Entry>.
                SummonedEntities.registerAttributes(entry.id, livingType, summonConfig.value::entryFor);
            }
        }
    }
}
