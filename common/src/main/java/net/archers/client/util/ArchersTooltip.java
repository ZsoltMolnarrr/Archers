package net.archers.client.util;

import net.archers.block.ArcherBlocks;
import net.archers.block.ArcherWorkbenchBlock;
import net.archers.item.misc.AutoFireHook;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class ArchersTooltip {
    /// Appends Archers' custom tooltip lines to an item's tooltip. Loader-neutral — each platform's client
    /// entrypoint calls this from its own tooltip event (Fabric `ItemTooltipCallback`; NeoForge
    /// `ItemTooltipEvent`), so `common` needs no Fabric API client tooltip callback.
    public static void addLines(ItemStack itemStack, List<Component> lines) {
        if (AutoFireHook.isApplied(itemStack)) {
            lines.add(1, Component.translatable(AutoFireHook.item.getDescriptionId()).withStyle(ChatFormatting.DARK_GREEN));
        }
        // `Block#appendTooltip` was removed in 1.21.11, so the workbench hint is appended here instead.
        if (itemStack.is(ArcherBlocks.WORKBENCH.block().asItem())) {
            var id = ArcherWorkbenchBlock.ID;
            lines.add(Component.translatable("block." + id.getNamespace() + "." + id.getPath() + ".hint")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
