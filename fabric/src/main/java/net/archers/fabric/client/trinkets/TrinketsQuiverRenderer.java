package net.archers.fabric.client.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.archers.ArchersMod;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.render.CustomLayers;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/// Renders a quiver on the wearer's back.
///
/// 1.21.11 notes: Trinkets' renderer hook is render-state + queue based, and Spell Engine's
/// `ItemRendererAccessor#SpellEngine_renderBakedItemModel` is gone with the 1.21.4 item-model
/// overhaul — the quiver model is a keyed extra model (registered from `ArchersClientMod` via
/// `CustomModels.registerModelIds`) submitted to the render command queue by [CustomModels#render].
public class TrinketsQuiverRenderer implements TrinketRenderer {
    private final Identifier modelId;

    public TrinketsQuiverRenderer(String modelName) {
        this.modelId = Identifier.of(ArchersMod.ID, "item/quiver/" + modelName);
    }

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
            Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(-140), //Degrees of rotation
                    new Vector3f(1, 0, 0)); //Rotate around the X axis

            TrinketRenderer.followBodyRotations(entityModel, bipedModel); //Don´t know if it makes any difference but might as well leave it here
            TrinketRenderer.translateToChest(matrixStack, bipedModel, bipedState);
            matrixStack.translate(-0.825F, 0.25F, 0.7F); //Position
            matrixStack.multiply(rotation);
        }
        CustomModels.render(CustomLayers.spellObject(LightEmission.NONE), this.modelId,
                matrixStack, queue, light, 0);
    }
}
