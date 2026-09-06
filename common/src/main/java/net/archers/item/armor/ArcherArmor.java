package net.archers.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.spell_engine.rpg_series.item.Armor;

public class ArcherArmor extends Armor.CustomItem {
    public ArcherArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    public static ArcherArmor archer(ArmorMaterial material, Type slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }

    public static ArcherArmor ranger(ArmorMaterial material, Type slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }
}
