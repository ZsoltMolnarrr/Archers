package net.archers.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_engine.api.spell.summon.SummonedEntities;
import net.spell_engine.api.spell.summon.SummonedEntityConfig;

public class ArcherEntities {

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
        SpiritWolfEntity.TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                SpiritWolfEntity.ID,
                EntityType.Builder.<SpiritWolfEntity>create(SpiritWolfEntity::new, SpawnGroup.MISC)
                        // dimensions(float, float) yields `changing` (fixed=false) so
                        // EntityDimensions.scaled() applies GENERIC_SCALE if one is ever added
                        // Halfway between the vanilla wolf (0.6 x 0.85) and the original 1x1
                        .dimensions(0.8F, 0.925F)
                        .maxTrackingRange(64)
                        .trackingTickInterval(3)
                        .build()
        );
        SummonedEntities.registerAttributes(SpiritWolfEntity.ID, SpiritWolfEntity.TYPE, spiritWolfDefaults());
    }
}
