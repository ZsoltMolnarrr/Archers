package net.archers.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

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
    private static final ItemStackRenderState renderState = new ItemStackRenderState();

    public static void render(ItemStack stack, HumanoidModel<?> model, HumanoidRenderState state,
                              PoseStack matrices, SubmitNodeCollector queue, int light) {
        var client = Minecraft.getInstance();
        client.getItemModelResolver().updateForTopItem(renderState, stack, ItemDisplayContext.NONE, client.level, null, 0);
        if (renderState.isEmpty()) {
            return;
        }
        matrices.pushPose();
        // Trinkets' `translateToChest`
        if (state.isCrouching && !state.isPassenger && !state.isVisuallySwimming) {
            matrices.translate(0.0F, 0.2F, 0.0F);
            matrices.mulPose(Axis.XP.rotation(model.body.xRot));
        }
        matrices.mulPose(Axis.YP.rotation(model.body.yRot));
        matrices.translate(0.0F, 0.4F, -0.16F);
        // Placement tuned on 1.21.1 (model in [0,1]^3 space)
        matrices.translate(-0.825F, 0.25F, 0.7F);
        matrices.mulPose(Axis.XP.rotationDegrees(-140));
        // Cancel the identity display transform's centring translate (see class doc)
        matrices.translate(0.5F, 0.5F, 0.5F);
        renderState.submit(matrices, queue, light, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();
    }
}
