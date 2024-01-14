package net.archers.config;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.api.item.ItemConfig;

import java.util.HashMap;
import java.util.Map;

public class ArchersItemConfig { public ArchersItemConfig() {}
    public Map<String, RangedConfig> ranged_weapons = new HashMap();
    public Map<String, ItemConfig.Weapon> melee_weapons = new HashMap();
    public Map<String, ItemConfig.ArmorSet> armor_sets = new HashMap();
}