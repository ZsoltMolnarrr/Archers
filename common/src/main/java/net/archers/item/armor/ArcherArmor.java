package net.archers.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.rpg_series.item.Armor;

public class ArcherArmor extends Armor.CustomItem {
    public ArcherArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    public static ArcherArmor archer(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }

    public static ArcherArmor ranger(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }
}
