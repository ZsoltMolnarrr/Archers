package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.spell_engine.client.compatibility.ShaderCompatibility;

public class DirewolfEntityRenderer extends MobEntityRenderer<SpiritWolfEntity, DirewolfEntityModel> {
    public static final Identifier TEXTURE =
            new Identifier(ArchersMod.ID, "textures/entity/direwolf_spell.png");

    public DirewolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DirewolfEntityModel(context.getPart(DirewolfEntityModel.TEXTURE)), 0.5f);
    }

    @Override
    public Identifier getTexture(SpiritWolfEntity entity) {
        return TEXTURE;
    }

    // With shaders active the model drops from the emissive to the lightmap-respecting
    // translucent layer (see DirewolfEntityModel); a boosted block light keeps the spirit
    // glowing in darkness without triggering shaderpack bloom.
    @Override
    protected int getBlockLight(SpiritWolfEntity entity, BlockPos pos) {
        if (ShaderCompatibility.isShaderPackInUse()) {
            return Math.max(12, super.getBlockLight(entity, pos));
        }
        return super.getBlockLight(entity, pos);
    }
}
