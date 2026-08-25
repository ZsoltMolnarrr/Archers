package net.archers.client.util;

import net.archers.block.ArcherBlocks;
import net.archers.block.ArcherWorkbenchBlock;
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
        // `Block#appendTooltip` was removed in 1.21.11, so the workbench hint is appended here instead.
        if (itemStack.isOf(ArcherBlocks.WORKBENCH.block().asItem())) {
            var id = ArcherWorkbenchBlock.ID;
            lines.add(Text.translatable("block." + id.getNamespace() + "." + id.getPath() + ".hint")
                    .formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    }
}
