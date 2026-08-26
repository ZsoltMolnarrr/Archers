package net.archers.item.misc;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class AutoFireHookItem extends Item {
    public AutoFireHookItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable(this.getDescriptionId() + ".description_1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable(this.getDescriptionId() + ".description_2").withStyle(ChatFormatting.GRAY));
    }

}
