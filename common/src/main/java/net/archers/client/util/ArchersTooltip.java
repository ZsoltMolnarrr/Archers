package net.archers.client.util;

import net.archers.item.misc.AutoFireHook;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ArchersTooltip {
    /// Appends Archers' custom tooltip lines to an item's tooltip. Loader-neutral — each platform's client
    /// entrypoint calls this from its own tooltip event (Fabric `ItemTooltipCallback`; NeoForge
    /// `ItemTooltipEvent`), so `common` needs no Fabric API client tooltip callback.
    public static void addLines(ItemStack itemStack, List<Text> lines) {
        if (AutoFireHook.isApplied(itemStack)) {
            lines.add(1, Text.translatable(AutoFireHook.item.getTranslationKey()).formatted(Formatting.DARK_GREEN));
        }
    }
}
