package net.archers.client.util;

import com.ibm.icu.text.DecimalFormat;
import net.archers.item.misc.AutoFireHook;
import net.archers.item.weapon.CustomRangedWeaponProperties;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class ArchersTooltip {
    public static void init() {
        ItemTooltipCallback.EVENT.register((itemStack, context, lines) -> {
            if (AutoFireHook.isApplied(itemStack)) {
                lines.add(1, Text.translatable(AutoFireHook.item.getTranslationKey()).formatted(Formatting.DARK_GREEN));
            }
        });
    }

    public static void addPullTime(ItemStack itemStack, List<Text> lines) {
        var pullTime = readablePullTime(itemStack);
        if (pullTime > 0) {
            int lastAttributeLine = getLastAttributeLine(lines);

            if (lastAttributeLine > 0) {
                lines.add(lastAttributeLine + 1,
                        Text.literal(" ").append(
                                Text.translatable("item.ranged_weapon.pull_time", formattedNumber(pullTime / 20F))
                                        .formatted(Formatting.DARK_GREEN)
                        )
                );
            }
        }
    }

    private static int getLastAttributeLine(List<Text> lines) {
        int lastAttributeLine = -1;
        var attributePrefix = "attribute.modifier";
        var handPrefix = "item.modifiers";
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            var content = line.getContent();
            // Is this a line like "+1 Something"
            if (content instanceof TranslatableTextContent translatableText) {
                var key = translatableText.getKey();
                if (key.startsWith(attributePrefix) || key.startsWith(handPrefix)) {
                    lastAttributeLine = i;
                }
            }
        }
        return lastAttributeLine;
    }

    private static int readablePullTime(ItemStack itemStack) {
        var item = itemStack.getItem();
        if (item instanceof CrossbowItem) {
            return CrossbowItem.getPullTime(itemStack);
        } else {
            if (itemStack.isOf(Items.BOW)) {
                return 20;
            } else if (item instanceof CustomRangedWeaponProperties customBow) {
                return customBow.getCustomPullTime_RPGS();
            }
        }
        return 0;
    }

    private static String formattedNumber(float number) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(1);
        return formatter.format(number);
    }
}
