package net.archers.client;

import net.archers.item.AutoFireHook;
import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomCrossbow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ArchersClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var bow: CustomBow.instances) {
            ModelPredicateHelper.registerBowModelPredicates(bow);
        }
        for (var crossbow: CustomCrossbow.instances) {
            ModelPredicateHelper.registerCrossbowModelPredicates(crossbow);
        }
        ItemTooltipCallback.EVENT.register((itemStack, context, lines) -> {
            if (AutoFireHook.isApplied(itemStack)) {
                lines.add(1, Text.translatable(AutoFireHook.item.getTranslationKey()).formatted(Formatting.DARK_GREEN));
            }
        });
    }
}
