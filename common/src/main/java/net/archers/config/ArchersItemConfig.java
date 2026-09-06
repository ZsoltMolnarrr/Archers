package net.archers.config;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.tiny_config.versioning.VersionableConfig;

import java.util.LinkedHashMap;

/// `config/archers/equipment.json`.
///
/// Versioned since schema 1: RangedWeaponAPI 2.x changed the `RangedConfig` record that
/// {@link #ranged_weapons} serialises — `pull_time` (absolute ticks) became `pull_time_bonus`
/// (seconds offset from a 1 s baseline) and `velocity` (absolute) became `velocity_bonus`. GSON would
/// silently read both missing fields as `0` from a pre-2.x file, giving every bow a 1 s pull and no
/// velocity bonus, so the schema version discards such a file and the defaults are re-seeded instead.
public class ArchersItemConfig extends VersionableConfig { public ArchersItemConfig() {}
    /// Bump when a field's meaning or units change (see the class doc). 1 = RangedWeaponAPI 2.x shape.
    public static final int SCHEMA_VERSION = 1;

    public LinkedHashMap<String, RangedConfig> ranged_weapons = new LinkedHashMap();
    public LinkedHashMap<String, WeaponConfig> melee_weapons = new LinkedHashMap();
    public LinkedHashMap<String, ArmorSetConfig> armor_sets = new LinkedHashMap();
}
