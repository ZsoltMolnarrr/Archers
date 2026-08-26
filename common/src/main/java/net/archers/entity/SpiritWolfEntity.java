package net.archers.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.spell_engine.entity.SummonedEntity;

public class SpiritWolfEntity extends SummonedEntity {

    public SpiritWolfEntity(EntityType<? extends SpiritWolfEntity> entityType, Level world) {
        super(entityType, world);
    }
}
