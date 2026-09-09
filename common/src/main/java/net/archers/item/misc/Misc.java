package net.archers.item.misc;

import net.archers.item.Quivers;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.Platform;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Misc {
    public record Entry(Identifier id, Item item) { }
    /// Public so each loader's entrypoint can add these to the Archers creative tab (see the per-platform
    /// BuildCreativeModeTabContentsEvent / ItemGroupEvents registration).
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Identifier id, Item item) {
        var entry = new Entry(id, item);
        ENTRIES.add(entry);
        return entry;
    }
    public static Entry autoFireHook = add(AutoFireHook.id, AutoFireHook.item);

    /// The misc items — plus the quivers when BundleAPI is present — keyed by the id they register under.
    /// Creation only: a loader that registers items itself (Forge) iterates this instead of calling
    /// {@link #register()}. Must run inside the `ITEM` registration window (see `Quivers#itemsToRegister`).
    public static Map<Identifier, Item> itemsToRegister() {
        var items = new LinkedHashMap<Identifier, Item>();
        for (var entry: ENTRIES) {
            items.put(entry.id, entry.item);
        }
        if (Platform.util().isModLoaded("bundleapi")) {
            items.putAll(Quivers.itemsToRegister());
        }
        return items;
    }

    public static void register() {
        itemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
        // Creative-tab placement is registered per-platform from each loader's entrypoint.
    }
}
