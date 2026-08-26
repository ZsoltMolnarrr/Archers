package net.archers.item.armor;

import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.spell_engine.rpg_series.item.Armor;

public class ArcherArmor extends Armor.CustomItem {
    public ArcherArmor(ArmorMaterial material, ArmorType slot, Properties settings) {
        super(material, slot, settings);
    }

    public static ArcherArmor archer(ArmorMaterial material, ArmorType slot, Properties settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }

    public static ArcherArmor ranger(ArmorMaterial material, ArmorType slot, Properties settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }
}
