package net.archers.item.misc;

import net.archers.item.Group;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Misc {
    public record Entry(Identifier id, Item item) { }
    private static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Identifier id, Item item) {
        var entry = new Entry(id, item);
        ENTRIES.add(entry);
        return entry;
    }
    public static Entry autoFireHook = add(AutoFireHook.id, AutoFireHook.item);

    public static void register() {
        for (var entry: ENTRIES) {
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: ENTRIES) {
                content.add(entry.item);
            }
        });
    }
}
