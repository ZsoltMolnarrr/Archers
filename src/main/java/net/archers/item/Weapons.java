package net.archers.item;

import net.archers.ArchersMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Weapons {
    public static final ArrayList<Entry> all = new ArrayList<>();
    public record Entry(Identifier id, CustomBow item, CustomBow.Config defaults) { }
    private static Entry add(Identifier id, CustomBow item, CustomBow.Config defaults) {
        var entry = new Entry(id, item, defaults);
        item.configure(defaults);
        all.add(entry);
        return entry;
    }

    public static Entry mechanic_shortbow = add(
        new Identifier(ArchersMod.ID, "mechanic_shortbow"),
        new CustomBow(new FabricItemSettings().maxDamage(384)),
        new CustomBow.Config(20, 4, 1.5F)
    );

    public static void register() {
        for (var entry: all) {
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: all) {
                content.add(entry.item);
            }
        });
    }
}
