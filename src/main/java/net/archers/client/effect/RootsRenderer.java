package net.archers.client.effect;

import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

public class RootsRenderer implements CustomModelStatusEffect.Renderer {

    // MARK: Renderer
    private static final RenderLayer RENDER_LAYER = TexturedRenderLayers.getEntityCutout();

    public static final Identifier modelId = new Identifier(ArchersMod.ID, "effect/entangling_roots");
    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        CustomModels.render(RENDER_LAYER, MinecraftClient.getInstance().getItemRenderer(), modelId,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
