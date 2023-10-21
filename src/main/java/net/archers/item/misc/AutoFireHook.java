package net.archers.item.misc;

import net.archers.ArchersMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AutoFireHook extends Item {
    public static final Identifier id = new Identifier(ArchersMod.ID, "auto_fire_hook");
    public static final Item item = new AutoFireHook((new FabricItemSettings()).maxCount(1));
    public static final String NBT_KEY = ArchersMod.ID + ":afh";
    public static final TagKey<Item> crossbowsTag = TagKey.of(RegistryKeys.ITEM, new Identifier(ArchersMod.ID, "auto_fire_hook_attachables"));
    public static boolean isApplied(ItemStack itemStack) {
        return itemStack.hasNbt() && itemStack.getNbt().contains(NBT_KEY);
    }

    public static void apply(ItemStack itemStack) {
        itemStack.getOrCreateNbt().putBoolean(NBT_KEY, true);
    }

    public static boolean isApplicable(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) { return false; }
        return (itemStack.getItem() instanceof CrossbowItem || itemStack.isIn(crossbowsTag))
                && !isApplied(itemStack);
    }

    public AutoFireHook(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".description_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".description_2").formatted(Formatting.GRAY));
    }
}
