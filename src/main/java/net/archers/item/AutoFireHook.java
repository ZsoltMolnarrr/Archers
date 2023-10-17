package net.archers.item;

import net.archers.ArchersMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AutoFireHook {
    public static final Identifier id = new Identifier(ArchersMod.ID, "auto_fire_hook");
    public static final Item item = new Item((new FabricItemSettings()).maxCount(1));
    public static final String NBT_KEY = ArchersMod.ID + ":afh";
    public static final TagKey<Item> crossbowsTag = TagKey.of(RegistryKeys.ITEM, new Identifier(ArchersMod.ID, "crossbows"));

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
}
