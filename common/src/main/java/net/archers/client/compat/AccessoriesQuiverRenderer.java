package net.archers.client.compat;

import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.archers.ArchersMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_engine.Platform;
import net.spell_engine.mixin.client.render.ItemRendererAccessor;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AccessoriesQuiverRenderer implements AccessoryRenderer {
    private final Identifier modelId;
    private ModelIdentifier modelIdentifier;

    public AccessoriesQuiverRenderer(String modelName) {
        var id = Identifier.of(ArchersMod.ID, "item/quiver/" + modelName);
        this.modelId = id;
        if (!Platform.Fabric) {
            this.modelIdentifier = new ModelIdentifier(modelId, "standalone");
        }
    }

    @Override
    public <M extends LivingEntity> void render(ItemStack itemStack,
                                                SlotReference slotReference,
                                                MatrixStack matrixStack,
                                                EntityModel<M> entityModel,
                                                VertexConsumerProvider vertexConsumerProvider,
                                                int light, float v, float v1, float v2, float v3, float v4, float v5) {
        var client = MinecraftClient.getInstance();
        var manager = client.getBakedModelManager();
        BakedModel model;
        if (modelIdentifier != null) {
            model = manager.getModel(modelIdentifier);
        } else {
            model = manager.getModel(modelId);
        }
        if (entityModel instanceof PlayerEntityModel playerModel) {
            AccessoryRenderer.transformToModelPart(matrixStack, playerModel.body);
            matrixStack.translate(-1.65F, -0.5F ,-1.2F); //Position
            matrixStack.scale(2F, 2F, 2F);

            Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(40), //Degrees of rotation
                    new Vector3f(1,0,0)); //Rotate around the X axis
            matrixStack.multiply(rotation);
        }
        var buffer = vertexConsumerProvider.getBuffer(RenderLayers.getItemLayer(itemStack, true));
        ((ItemRendererAccessor)client.getItemRenderer()).SpellEngine_renderBakedItemModel(model, ItemStack.EMPTY, light, OverlayTexture.DEFAULT_UV, matrixStack, buffer);
    }
}