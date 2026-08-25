package net.archers.client.effect;

import net.archers.ArchersMod;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

public class RootsRenderer implements CustomModelStatusEffect.Renderer {

    // MARK: Renderer

    public static final Identifier modelId = Identifier.of(ArchersMod.ID, "spell_effect/entangling_roots");
    @Override
    public void renderEffect(long appliedAtWorldTime, int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        CustomModels.render(TexturedRenderLayers.getEntityCutout(), modelId, matrixStack, queue, light, livingEntity.getId());
        matrixStack.pop();
    }
}
