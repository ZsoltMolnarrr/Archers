package net.archers.neoforge.client.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import net.archers.client.render.WornQuiverRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/// Renders a quiver on the wearer's back (Curios slot), see [WornQuiverRenderer].
public class CuriosQuiverRenderer implements ICurioRenderer {
    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            SubmitNodeCollector queue,
            int light,
            S state,
            RenderLayerParent<S, M> renderLayerParent,
            EntityRendererProvider.Context context,
            float limbSwing,
            float limbSwingAmount) {
        if (state instanceof HumanoidRenderState bipedState
                && renderLayerParent.getModel() instanceof HumanoidModel<?> bipedModel) {
            WornQuiverRenderer.render(stack, bipedModel, bipedState, matrixStack, queue, light);
        }
    }
}
