package net.archers.client.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.archers.ArchersMod;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

public class RootsRenderer implements CustomModelStatusEffect.Renderer {

    // MARK: Renderer

    public static final Identifier modelId = Identifier.fromNamespaceAndPath(ArchersMod.ID, "spell_effect/entangling_roots");
    @Override
    public void renderEffect(long appliedAtWorldTime, int amplifier, LivingEntity livingEntity, float delta, PoseStack matrixStack, SubmitNodeCollector queue, int light) {
        matrixStack.pushPose();
        matrixStack.translate(0, 0.5, 0);
        CustomModels.render(Sheets.cutoutBlockSheet(), modelId, matrixStack, queue, light, livingEntity.getId());
        matrixStack.popPose();
    }
}
