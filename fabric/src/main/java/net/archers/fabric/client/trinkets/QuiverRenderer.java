package net.archers.fabric.client.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_engine.mixin.client.render.ItemRendererAccessor;

public class QuiverRenderer implements TrinketRenderer {
    public static final Identifier modelId = Identifier.of(ArchersMod.ID, "quiver/quiver");
    @Override
    public void render(ItemStack itemStack,
                       SlotReference slotReference,
                       EntityModel<? extends LivingEntity> entityModel,
                       MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider,
                       int light, LivingEntity livingEntity,
                       float limbAngle, float limbDistance, float animationProgress, float customAngle, float headYaw, float headPitch) {
        var client = MinecraftClient.getInstance();
        var manager = client.getBakedModelManager();
        var model = manager.getModel(modelId);
        if (livingEntity instanceof AbstractClientPlayerEntity player && entityModel instanceof PlayerEntityModel playerModel) {
            TrinketRenderer.translateToRightLeg(matrixStack, playerModel, player);
        }
        var buffer = vertexConsumerProvider.getBuffer(RenderLayers.getItemLayer(itemStack, true));
        ((ItemRendererAccessor)client.getItemRenderer()).SpellEngine_renderBakedItemModel(model, ItemStack.EMPTY, light, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
    }
}
