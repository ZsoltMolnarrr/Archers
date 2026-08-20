package net.archers.neoforge.client.curios;

import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.mixin.client.render.ItemRendererAccessor;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosQuiverRenderer implements ICurioRenderer {
    private final ModelIdentifier modelIdentifier;

    public CuriosQuiverRenderer(String modelName) {
        this.modelIdentifier = new ModelIdentifier(Identifier.of(ArchersMod.ID, "item/quiver/" + modelName), "standalone");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          MatrixStack matrixStack,
                                                                          FeatureRendererContext<T, M> renderLayerParent,
                                                                          VertexConsumerProvider renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks, float netHeadYaw,
                                                                          float headPitch) {
        var client = MinecraftClient.getInstance();
        var model = client.getBakedModelManager().getModel(this.modelIdentifier);
        if (slotContext.entity() instanceof AbstractClientPlayerEntity player
                && renderLayerParent.getModel() instanceof PlayerEntityModel playerModel) {
            // Inlined equivalent of Trinkets' `TrinketRenderer.translateToChest`, so the
            // placement below can reuse the offsets tuned for the Fabric renderer.
            if (player.isInSneakingPose() && !playerModel.riding && !player.isSwimming()) {
                matrixStack.translate(0.0F, 0.2F, 0.0F);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(playerModel.body.pitch));
            }
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(playerModel.body.yaw));
            matrixStack.translate(0.0F, 0.4F, -0.16F);

            matrixStack.translate(-0.825F, 0.25F, 0.7F); //Position
            Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(-140), //Degrees of rotation
                    new Vector3f(1, 0, 0)); //Rotate around the X axis
            matrixStack.multiply(rotation);
        }
        var buffer = renderTypeBuffer.getBuffer(RenderLayers.getItemLayer(stack, true));
        ((ItemRendererAccessor) client.getItemRenderer()).SpellEngine_renderBakedItemModel(model, ItemStack.EMPTY, light, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
    }
}
