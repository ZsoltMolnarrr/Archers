package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.spell_engine.client.compatibility.ShaderCompatibility;

public class DirewolfEntityRenderer extends MobEntityRenderer<SpiritWolfEntity, SpiritWolfRenderState, DirewolfEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(ArchersMod.ID, "textures/entity/direwolf_spell.png");

    public DirewolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DirewolfEntityModel(context.getPart(DirewolfEntityModel.TEXTURE)), 0.5f);
    }

    @Override
    public SpiritWolfRenderState createRenderState() {
        return new SpiritWolfRenderState();
    }

    @Override
    public void updateRenderState(SpiritWolfEntity entity, SpiritWolfRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        state.spawnAnimationState.copyFrom(entity.spawnAnimationState);
        state.despawnAnimationState.copyFrom(entity.despawnAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.attackAnimationState.copyFrom(entity.attackAnimationState);
        state.attackVariant = entity.getAttackVariant();
        // Playback speed is derived from the clip length, so it must be resolved while the entity
        // is still reachable (the model only sees the render state).
        var attackAnimation = DirewolfEntityModel.attackAnimationFor(state.attackVariant);
        state.attackAnimationSpeed = entity.getAttackAnimationSpeed(attackAnimation.lengthInSeconds() * 20F);
    }

    @Override
    public Identifier getTexture(SpiritWolfRenderState state) {
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
