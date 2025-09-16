package net.archers.client.util;

import net.archers.item.misc.AutoFireHook;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ArchersTooltip {
    public static void init() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, lines) -> {
            if (AutoFireHook.isApplied(itemStack)) {
                lines.add(1, Text.translatable(AutoFireHook.item.getTranslationKey()).formatted(Formatting.DARK_GREEN));
            }
        });
    }
}
