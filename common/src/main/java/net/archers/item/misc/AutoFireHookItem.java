package net.archers.item.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AutoFireHookItem extends Item {
    public AutoFireHookItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".description_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".description_2").formatted(Formatting.GRAY));
    }

}
