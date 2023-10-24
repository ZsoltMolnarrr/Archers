package net.archers.client.effect;

import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

public class HuntersMarkRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId = new Identifier(ArchersMod.ID, "effect/hunters_mark");
    private static final RenderLayer GLOWING_RENDER_LAYER = CustomLayers.spellEffect(LightEmission.GLOW, false);
    //TexturedRenderLayers.getEntityCutout();

    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        if (livingEntity.getHealth() <= 0 || !livingEntity.isAlive()) { return; }
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        var camera =  MinecraftClient.getInstance().gameRenderer.getCamera();
        var direction = camera.getPos().subtract(livingEntity.getPos()).normalize().multiply(livingEntity.getWidth() * 0.5F);

        matrixStack.push();

        matrixStack.translate(direction.x, direction.y + livingEntity.getHeight() * 0.75F, direction.z);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F + (float)Math.toDegrees(Math.atan2(direction.x, direction.z)) ));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        CustomModels.render(GLOWING_RENDER_LAYER, itemRenderer, modelId,
                matrixStack, vertexConsumers, light, livingEntity.getId());

        matrixStack.pop();
    }
}
