package net.archers.item.armor;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.spell_engine.rpg_series.item.Armor;

public class ArcherArmor extends Armor.CustomItem {
    public ArcherArmor(ArmorMaterial material, EquipmentType slot, Settings settings) {
        super(material, slot, settings);
    }

    public static ArcherArmor archer(ArmorMaterial material, EquipmentType slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }

    public static ArcherArmor ranger(ArmorMaterial material, EquipmentType slot, Settings settings) {
        var armor = new ArcherArmor(material, slot, settings);
        return armor;
    }
}
