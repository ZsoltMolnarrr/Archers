package net.archers.item.misc;

import net.archers.ArchersMod;
import net.archers.component.ArcherComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AutoFireHook {
    public static final Identifier id = Identifier.fromNamespaceAndPath(ArchersMod.ID, "auto_fire_hook");
    // Since 1.21.2 an `Item.Settings` built outside `Items.register` must carry its registry key,
    // or the item crashes at construction ("Item id not set").
    public static final Item item = new AutoFireHookItem((new Item.Properties())
            .setId(ResourceKey.create(Registries.ITEM, id))
            .stacksTo(1));
    public static final TagKey<Item> AFH_ATTACHABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ArchersMod.ID, "auto_fire_hook_attachables"));

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
        return (itemStack.getItem() instanceof CrossbowItem || itemStack.is(AFH_ATTACHABLE))
                && !isApplied(itemStack);
    }
}
