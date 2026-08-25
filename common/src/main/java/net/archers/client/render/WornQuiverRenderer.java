package net.archers.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

/// Draws a quiver on the wearer's back, shared by the Trinkets (Fabric) and Curios (NeoForge) renderers.
///
/// The model comes from the quiver's regular item-model definition (`assets/archers/items/<quiver>.json`)
/// resolved for [ItemDisplayContext#NONE], i.e. the 3D `item/quiver/*` model with the identity display
/// transform — the definition is the single source of truth, no keyed extra model is needed.
///
/// Placement math: the matrix stack arrives in the entity's model space (after the renderer's
/// `scale(-1,-1,1)` + `translate(0,-1.501,0)`), the same frame the 1.21.1 feature renderers used. The
/// chest translation is Trinkets' `TrinketRenderer.translateToChest`, the offsets/rotation after it are
/// the values tuned on 1.21.1 for the raw `[0,1]^3` model. The vanilla item path applies the identity
/// transform as a `translate(-0.5,-0.5,-0.5)` (centres the unit cube), so the final `translate(0.5,0.5,0.5)`
/// cancels it: `M · T(+½) · T(-½) = M` — exactly the 1.21.1 placement.
public class WornQuiverRenderer {
    private static final ItemRenderState renderState = new ItemRenderState();

    public static void render(ItemStack stack, BipedEntityModel<?> model, BipedEntityRenderState state,
                              MatrixStack matrices, OrderedRenderCommandQueue queue, int light) {
        var client = MinecraftClient.getInstance();
        client.getItemModelManager().clearAndUpdate(renderState, stack, ItemDisplayContext.NONE, client.world, null, 0);
        if (renderState.isEmpty()) {
            return;
        }
        matrices.push();
        // Trinkets' `translateToChest`
        if (state.isInSneakingPose && !state.hasVehicle && !state.isSwimming) {
            matrices.translate(0.0F, 0.2F, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.body.pitch));
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(model.body.yaw));
        matrices.translate(0.0F, 0.4F, -0.16F);
        // Placement tuned on 1.21.1 (model in [0,1]^3 space)
        matrices.translate(-0.825F, 0.25F, 0.7F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-140));
        // Cancel the identity display transform's centring translate (see class doc)
        matrices.translate(0.5F, 0.5F, 0.5F);
        renderState.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }
}
