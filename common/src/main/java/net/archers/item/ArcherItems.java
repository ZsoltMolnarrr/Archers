package net.archers.item;

import net.minecraft.world.item.Item;
import net.spell_engine.rpg_series.item.Armor;

import java.util.HashMap;

public class ArcherItems {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: ArcherWeapons.rangedEntries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var weaponEntry: ArcherWeapons.meleeEntries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var entry: ArcherArmors.entries) {
            var set = entry.armorSet();
            for (var piece: set.pieces()) {
                var armorItem = (Armor.CustomItem) piece;
                entries.put(set.idOf(armorItem).toString(), armorItem);
            }
        }
    }
}
