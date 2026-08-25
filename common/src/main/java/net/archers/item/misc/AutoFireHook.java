package net.archers.item.misc;

import net.archers.ArchersMod;
import net.archers.component.ArcherComponents;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AutoFireHook {
    public static final Identifier id = Identifier.of(ArchersMod.ID, "auto_fire_hook");
    // Since 1.21.2 an `Item.Settings` built outside `Items.register` must carry its registry key,
    // or the item crashes at construction ("Item id not set").
    public static final Item item = new AutoFireHookItem((new Item.Settings())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, id))
            .maxCount(1));
    public static final TagKey<Item> AFH_ATTACHABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(ArchersMod.ID, "auto_fire_hook_attachables"));

    public static boolean isApplied(ItemStack itemStack) {
        var component = itemStack.get(ArcherComponents.AUTO_FIRE);
        if (component == null) { return false; }
        return component;
    }

    public static void apply(ItemStack itemStack) {
        itemStack.set(ArcherComponents.AUTO_FIRE, true);
    }
    public static void remove(ItemStack itemStack) {
        itemStack.remove(ArcherComponents.AUTO_FIRE);
    }

    public static boolean isApplicable(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) { return false; }
        return (itemStack.getItem() instanceof CrossbowItem || itemStack.isIn(AFH_ATTACHABLE))
                && !isApplied(itemStack);
    }
}
