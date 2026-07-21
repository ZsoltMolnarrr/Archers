package net.archers.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.spell_engine.entity.SummonedEntity;

public class SpiritWolfEntity extends SummonedEntity {

    public SpiritWolfEntity(EntityType<? extends SpiritWolfEntity> entityType, World world) {
        super(entityType, world);
    }
}
