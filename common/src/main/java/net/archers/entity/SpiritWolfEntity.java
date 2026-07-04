package net.archers.entity;

import net.archers.ArchersMod;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.entity.SummonedEntity;

public class SpiritWolfEntity extends SummonedEntity {
    public static final Identifier ID = Identifier.of(ArchersMod.ID, "spirit_wolf");
    public static EntityType<SpiritWolfEntity> TYPE;

    public SpiritWolfEntity(EntityType<? extends SpiritWolfEntity> entityType, World world) {
        super(entityType, world);
    }
}
