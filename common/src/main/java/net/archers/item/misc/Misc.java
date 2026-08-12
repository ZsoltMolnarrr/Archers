package net.archers.item.misc;

import net.archers.item.Quivers;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.Platform;

import java.util.ArrayList;

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

    public static void register() {
        for (var entry: ENTRIES) {
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        // Creative-tab placement is registered per-platform from each loader's entrypoint.
        if (Platform.util().isModLoaded("bundleapi")) {
            Quivers.register();
        }
    }
}
