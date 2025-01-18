package net.archers.client.armor;

import mod.azure.azurelibarmor.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelibarmor.rewrite.animation.impl.AzItemAnimator;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import net.archers.ArchersMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ArcherArmorRenderer extends AzArmorRenderer {
    public static ArcherArmorRenderer archer() {
        return new ArcherArmorRenderer("archer_armor", "archer_armor");
    }
    public static ArcherArmorRenderer ranger() {
        return new ArcherArmorRenderer("ranger_armor", "ranger_armor");
    }
    public static ArcherArmorRenderer netheriteRanger() {
        return new ArcherArmorRenderer("ranger_armor", "netherite_ranger_armor");
    }

    public static class ArcherArmorAnimator extends AzItemAnimator {
        private static final Identifier ANIMATIONS = Identifier.of(
                ArchersMod.ID,
                "animations/item/empty.animation.json"
        );

        @Override
        public void registerControllers(AzAnimationControllerContainer<ItemStack> animationControllerContainer) {}

        @Override
        public @NotNull Identifier getAnimationLocation(ItemStack animatable) {
            return ANIMATIONS;
        }
    }

    public ArcherArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(ArchersMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(ArchersMod.ID, "textures/armor/" + textureName + ".png")
        )
                .setAnimatorProvider(ArcherArmorAnimator::new)
                .build());
    }
}
