package net.archers.item.misc;

import net.archers.ArchersMod;
import net.archers.component.ArcherComponents;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AutoFireHook {
    public static final Identifier id = new Identifier(ArchersMod.ID, "auto_fire_hook");
    public static final Item item = new AutoFireHookItem((new Item.Settings()).maxCount(1));
    public static final TagKey<Item> AFH_ATTACHABLE = TagKey.of(RegistryKeys.ITEM, new Identifier(ArchersMod.ID, "auto_fire_hook_attachables"));

    public static boolean isApplied(ItemStack itemStack) {
        return ArcherComponents.getAutoFire(itemStack);
    }

    public static void apply(ItemStack itemStack) {
        ArcherComponents.setAutoFire(itemStack, true);
    }
    public static void remove(ItemStack itemStack) {
        ArcherComponents.removeAutoFire(itemStack);
    }

    public static boolean isApplicable(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) { return false; }
        return (itemStack.getItem() instanceof CrossbowItem || itemStack.isIn(AFH_ATTACHABLE))
                && !isApplied(itemStack);
    }
}
