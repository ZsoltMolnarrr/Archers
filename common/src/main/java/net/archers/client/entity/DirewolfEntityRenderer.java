package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class DirewolfEntityRenderer extends MobEntityRenderer<SpiritWolfEntity, DirewolfEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(ArchersMod.ID, "textures/entity/direwolf_spell.png");

    public DirewolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DirewolfEntityModel(context.getPart(DirewolfEntityModel.TEXTURE)), 0.5f);
    }

    @Override
    public Identifier getTexture(SpiritWolfEntity entity) {
        return TEXTURE;
    }
}
