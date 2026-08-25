package net.archers.neoforge.client.curios;

import net.archers.client.render.WornQuiverRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/// Renders a quiver on the wearer's back (Curios slot), see [WornQuiverRenderer].
public class CuriosQuiverRenderer implements ICurioRenderer {
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
            WornQuiverRenderer.render(stack, bipedModel, bipedState, matrixStack, queue, light);
        }
    }
}
