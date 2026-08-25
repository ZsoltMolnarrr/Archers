package net.archers.fabric.client.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.archers.client.render.WornQuiverRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

/// Renders a quiver on the wearer's back (Trinkets slot), see [WornQuiverRenderer].
public class TrinketsQuiverRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack,
                       SlotReference slotReference,
                       EntityModel<? extends LivingEntityRenderState> entityModel,
                       MatrixStack matrixStack,
                       OrderedRenderCommandQueue queue,
                       int light,
                       LivingEntityRenderState state,
                       float limbAngle, float limbDistance) {
        if (state instanceof BipedEntityRenderState bipedState && entityModel instanceof BipedEntityModel<?> bipedModel) {
            WornQuiverRenderer.render(itemStack, bipedModel, bipedState, matrixStack, queue, light);
        }
    }
}
