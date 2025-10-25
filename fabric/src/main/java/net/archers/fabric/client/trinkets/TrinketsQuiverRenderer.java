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
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TrinketsQuiverRenderer implements TrinketRenderer {
    private final Identifier modelId;

    public TrinketsQuiverRenderer(String modelName) {
        this.modelId = Identifier.of(ArchersMod.ID, "item/quiver/" + modelName);
    }

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
        var model = manager.getModel(this.modelId);
        if (livingEntity instanceof AbstractClientPlayerEntity player && entityModel instanceof PlayerEntityModel playerModel) {
            Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(-140), //Degrees of rotation
                    new Vector3f(1,0,0)); //Rotate around the X axis

            TrinketRenderer.followBodyRotations(player, playerModel); //Don´t know if it makes any difference but might as well leave it here
            TrinketRenderer.translateToChest(matrixStack, playerModel, player);
            matrixStack.translate(-0.825F, 0.25F ,0.7F); //Position
            matrixStack.multiply(rotation);
        }
        var buffer = vertexConsumerProvider.getBuffer(RenderLayers.getItemLayer(itemStack, true));
        ((ItemRendererAccessor)client.getItemRenderer()).SpellEngine_renderBakedItemModel(model, ItemStack.EMPTY, light, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
    }
}
