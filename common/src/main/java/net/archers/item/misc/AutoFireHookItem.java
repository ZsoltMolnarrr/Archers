package net.archers.item.misc;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public class AutoFireHookItem extends Item {
    public AutoFireHookItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Text.translatable(this.getTranslationKey() + ".description_1").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable(this.getTranslationKey() + ".description_2").formatted(Formatting.GRAY));
    }

}
