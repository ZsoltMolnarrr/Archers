package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.spell_engine.client.compatibility.ShaderCompatibility;

public class DirewolfEntityRenderer extends MobRenderer<SpiritWolfEntity, SpiritWolfRenderState, DirewolfEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "textures/entity/direwolf_spell.png");

    public DirewolfEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DirewolfEntityModel(context.bakeLayer(DirewolfEntityModel.TEXTURE)), 0.5f);
    }

    @Override
    public SpiritWolfRenderState createRenderState() {
        return new SpiritWolfRenderState();
    }

    @Override
    public void extractRenderState(SpiritWolfEntity entity, SpiritWolfRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
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
    public Identifier getTextureLocation(SpiritWolfRenderState state) {
        return TEXTURE;
    }

    // With shaders active the model drops from the emissive to the lightmap-respecting
    // translucent layer (see DirewolfEntityModel); a boosted block light keeps the spirit
    // glowing in darkness without triggering shaderpack bloom.
    @Override
    protected int getBlockLightLevel(SpiritWolfEntity entity, BlockPos pos) {
        if (ShaderCompatibility.isShaderPackInUse()) {
            return Math.max(12, super.getBlockLightLevel(entity, pos));
        }
        return super.getBlockLightLevel(entity, pos);
    }
}
