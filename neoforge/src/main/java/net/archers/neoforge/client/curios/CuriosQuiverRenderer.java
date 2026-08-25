package net.archers.neoforge.client.curios;

import net.archers.ArchersMod;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/// Renders a quiver on the wearer's back.
///
/// 1.21.11 notes: Curios 14's hook is render-state + queue based, and Spell Engine's
/// `ItemRendererAccessor#SpellEngine_renderBakedItemModel` is gone with the 1.21.4 item-model
/// overhaul — the quiver model is a keyed extra model (registered from `ArchersClientMod` via
/// `CustomModels.registerModelIds`) submitted to the render command queue by [CustomModels#render].
public class CuriosQuiverRenderer implements ICurioRenderer {
    private final Identifier modelId;

    public CuriosQuiverRenderer(String modelName) {
        this.modelId = Identifier.of(ArchersMod.ID, "item/quiver/" + modelName);
    }

    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack,
            SlotContext slotContext,
            MatrixStack matrixStack,
            OrderedRenderCommandQueue queue,
            int light,
            S state,
            FeatureRendererContext<S, M> renderLayerParent,
            EntityRendererFactory.Context context,
            float limbSwing,
            float limbSwingAmount) {
        if (state instanceof BipedEntityRenderState bipedState
                && renderLayerParent.getModel() instanceof BipedEntityModel<?> bipedModel) {
            // Inlined equivalent of Trinkets' `TrinketRenderer.translateToChest`, so the
            // placement below can reuse the offsets tuned for the Fabric renderer.
            if (bipedState.isInSneakingPose && !bipedState.hasVehicle && !bipedState.isSwimming) {
                matrixStack.translate(0.0F, 0.2F, 0.0F);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(bipedModel.body.pitch));
            }
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(bipedModel.body.yaw));
            matrixStack.translate(0.0F, 0.4F, -0.16F);

            matrixStack.translate(-0.825F, 0.25F, 0.7F); //Position
            Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(-140), //Degrees of rotation
                    new Vector3f(1, 0, 0)); //Rotate around the X axis
            matrixStack.multiply(rotation);
        }
        CustomModels.render(CustomLayers.spellObject(LightEmission.NONE), this.modelId,
                matrixStack, queue, light, 0);
    }
}
