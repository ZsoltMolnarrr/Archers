package net.archers.client.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

/// Per-frame snapshot of a [net.archers.entity.SpiritWolfEntity] (1.21.2+ split entity state from
/// rendering). Carries the summon's animation states plus the two attack values the model needs,
/// which are read off the entity in [DirewolfEntityRenderer#updateRenderState].
public class SpiritWolfRenderState extends LivingEntityRenderState {
    public final AnimationState spawnAnimationState = new AnimationState();
    public final AnimationState despawnAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackVariant = 0;
    public float attackAnimationSpeed = 1F;
}
