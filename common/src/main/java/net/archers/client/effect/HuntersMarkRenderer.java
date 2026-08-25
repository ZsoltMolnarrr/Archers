package net.archers.client.effect;

import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

public class HuntersMarkRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId = Identifier.of(ArchersMod.ID, "spell_effect/hunters_mark");
    private static final RenderLayer GLOWING_RENDER_LAYER = CustomLayers.spellEffect(LightEmission.GLOW, false);

    @Override
    public void renderEffect(long appliedAtWorldTime, int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light) {
        if (livingEntity.getHealth() <= 0 || !livingEntity.isAlive()) { return; }
        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        var direction = camera.getCameraPos().subtract(livingEntity.getEntityPos()).normalize().multiply(livingEntity.getWidth() * 0.5F);

        matrixStack.push();
        var verticalOffset = (livingEntity.getHeight() / livingEntity.getScale()) * 0.75F;
        matrixStack.translate(direction.x, verticalOffset, direction.z);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F + (float)Math.toDegrees(Math.atan2(direction.x, direction.z)) ));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        CustomModels.render(GLOWING_RENDER_LAYER, modelId, matrixStack, queue, light, livingEntity.getId());

        matrixStack.pop();
    }
}
