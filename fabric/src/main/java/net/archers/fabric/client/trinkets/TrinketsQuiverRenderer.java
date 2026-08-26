package net.archers.fabric.client.trinkets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.archers.client.render.WornQuiverRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;

/// Renders a quiver on the wearer's back (Trinkets slot), see [WornQuiverRenderer].
public class TrinketsQuiverRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack,
                       SlotReference slotReference,
                       EntityModel<? extends LivingEntityRenderState> entityModel,
                       PoseStack matrixStack,
                       SubmitNodeCollector queue,
                       int light,
                       LivingEntityRenderState state,
                       float limbAngle, float limbDistance) {
        if (state instanceof HumanoidRenderState bipedState && entityModel instanceof HumanoidModel<?> bipedModel) {
            WornQuiverRenderer.render(itemStack, bipedModel, bipedState, matrixStack, queue, light);
        }
    }
}
