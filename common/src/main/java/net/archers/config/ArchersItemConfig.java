package net.archers.config;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.WeaponConfig;

import java.util.LinkedHashMap;

public class ArchersItemConfig { public ArchersItemConfig() {}
    public LinkedHashMap<String, RangedConfig> ranged_weapons = new LinkedHashMap();
    public LinkedHashMap<String, WeaponConfig> melee_weapons = new LinkedHashMap();
    public LinkedHashMap<String, ArmorSetConfig> armor_sets = new LinkedHashMap();
}