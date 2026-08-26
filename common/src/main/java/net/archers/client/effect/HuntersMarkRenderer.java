package net.archers.client.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.archers.ArchersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

public class HuntersMarkRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId = Identifier.fromNamespaceAndPath(ArchersMod.ID, "spell_effect/hunters_mark");
    private static final RenderType GLOWING_RENDER_LAYER = CustomLayers.spellEffect(LightEmission.GLOW, false);

    @Override
    public void renderEffect(long appliedAtWorldTime, int amplifier, LivingEntity livingEntity, float delta, PoseStack matrixStack, SubmitNodeCollector queue, int light) {
        if (livingEntity.getHealth() <= 0 || !livingEntity.isAlive()) { return; }
        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        var direction = camera.position().subtract(livingEntity.position()).normalize().scale(livingEntity.getBbWidth() * 0.5F);

        matrixStack.pushPose();
        var verticalOffset = (livingEntity.getBbHeight() / livingEntity.getScale()) * 0.75F;
        matrixStack.translate(direction.x, verticalOffset, direction.z);

        matrixStack.mulPose(Axis.YP.rotationDegrees(180F + (float)Math.toDegrees(Math.atan2(direction.x, direction.z)) ));
        matrixStack.mulPose(Axis.XP.rotationDegrees(camera.xRot()));

        CustomModels.render(GLOWING_RENDER_LAYER, modelId, matrixStack, queue, light, livingEntity.getId());

        matrixStack.popPose();
    }
}
