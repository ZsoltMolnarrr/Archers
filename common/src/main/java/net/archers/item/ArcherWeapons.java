package net.archers.item;

import net.archers.ArchersMod;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.Platform;
import net.minecraft.registry.tag.ItemTags;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.RangedWeapon;
import net.spell_engine.rpg_series.item.RangedWeapons;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.rpg_series.item.Weapons;

import java.util.ArrayList;
import java.util.Map;

public class ArcherWeapons {
    private static final String NAMESPACE = ArchersMod.ID;
    public static final ArrayList<RangedWeapon.Entry> rangedEntries = new ArrayList<>();
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();

    private static RangedWeapon.Entry addRanged(RangedWeapon.Entry entry) {
        rangedEntries.add(entry);
        return entry;
    }

    private static Weapon.Entry addMelee(Weapon.Entry entry) {
        meleeEntries.add(entry);
        return entry;
    }

    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String AETHER = "aether";

    // MARK: Spears

    public static final Weapon.Entry flint_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "flint_spear", Equipment.Tier.TIER_0, ArcherItemTags.REPAIRS_FLINT)
            .translatedName("Flint Spear"));
    public static final Weapon.Entry iron_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "iron_spear", Equipment.Tier.TIER_1, null)
            .translatedName("Iron Spear"));
    public static final Weapon.Entry golden_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "golden_spear", Equipment.Tier.GOLDEN, null)
            .loot(Equipment.LootProperties.of("golden_weapon"))
            .translatedName("Golden Spear"));
    public static final Weapon.Entry diamond_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "diamond_spear", Equipment.Tier.TIER_2, null)
            .translatedName("Diamond Spear"));
    public static final Weapon.Entry netherite_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "netherite_spear", Equipment.Tier.TIER_3, null)
            .translatedName("Netherite Spear"));

    // MARK: Bows

    public static final RangedWeapon.Entry composite_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "composite_longbow", Equipment.Tier.TIER_1, ArcherItemTags.REPAIRS_BONE)
            .translatedName("Composite Longbow"));
    public static final RangedWeapon.Entry mechanic_shortbow = addRanged(RangedWeapons.shortBow(
            NAMESPACE, "mechanic_shortbow", Equipment.Tier.TIER_2, ArcherItemTags.REPAIRS_REDSTONE)
            .translatedName("Mechanical Shortbow"));
    public static final RangedWeapon.Entry royal_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "royal_longbow", Equipment.Tier.TIER_2, ItemTags.GOLD_TOOL_MATERIALS)
            .translatedName("Royal Longbow"));
    public static final RangedWeapon.Entry netherite_shortbow = addRanged(RangedWeapons.shortBow(
            NAMESPACE, "netherite_shortbow", Equipment.Tier.TIER_3, ItemTags.NETHERITE_TOOL_MATERIALS)
            .translatedName("Netherite Shortbow"));
    public static final RangedWeapon.Entry netherite_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "netherite_longbow", Equipment.Tier.TIER_3, ItemTags.NETHERITE_TOOL_MATERIALS)
            .translatedName("Netherite Longbow"));

    // MARK: Crossbows

    public static final RangedWeapon.Entry rapid_crossbow = addRanged(RangedWeapons.rapidCrossbow(
            NAMESPACE, "rapid_crossbow", Equipment.Tier.TIER_2, ArcherItemTags.REPAIRS_REDSTONE)
            .translatedName("Rapid Crossbow"));
    public static final RangedWeapon.Entry heavy_crossbow = addRanged(RangedWeapons.heavyCrossbow(
            NAMESPACE, "heavy_crossbow", Equipment.Tier.TIER_2, ItemTags.DIAMOND_TOOL_MATERIALS)
            .translatedName("Heavy Crossbow"));
    public static final RangedWeapon.Entry netherite_rapid_crossbow = addRanged(RangedWeapons.rapidCrossbow(
            NAMESPACE, "netherite_rapid_crossbow", Equipment.Tier.TIER_3, ItemTags.NETHERITE_TOOL_MATERIALS)
            .translatedName("Netherite Rapid Crossbow"));
    public static final RangedWeapon.Entry netherite_heavy_crossbow = addRanged(RangedWeapons.heavyCrossbow(
            NAMESPACE, "netherite_heavy_crossbow", Equipment.Tier.TIER_3, ItemTags.NETHERITE_TOOL_MATERIALS)
            .translatedName("Netherite Heavy Crossbow"));

    // MARK: Register

    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, WeaponConfig> meleeConfig) {
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            addMelee(Weapons.spearWithSkill(NAMESPACE, "aeternium_spear", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_AETERNIUM));
            addRanged(RangedWeapons.shortBow(NAMESPACE, "crystal_shortbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_CRYSTAL_SHARDS));
            addRanged(RangedWeapons.longBow(NAMESPACE, "crystal_longbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_CRYSTAL_SHARDS));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            addMelee(Weapons.spearWithSkill(NAMESPACE, "ruby_spear", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_NETHER_RUBY));
            addRanged(RangedWeapons.rapidCrossbow(NAMESPACE, "ruby_rapid_crossbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_NETHER_RUBY));
            addRanged(RangedWeapons.heavyCrossbow(NAMESPACE, "ruby_heavy_crossbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_NETHER_RUBY));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            addMelee(Weapons.spearWithSkill(NAMESPACE, "aether_spear", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_AMBROSIUM)
                    .loot(Equipment.LootProperties.of("aether")));
            addRanged(RangedWeapons.longBow(NAMESPACE, "aether_longbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_AMBROSIUM).loot(-1, "aether"));
            addRanged(RangedWeapons.rapidCrossbow(NAMESPACE, "aether_rapid_crossbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_AMBROSIUM).loot(-1, "aether"));
            addRanged(RangedWeapons.heavyCrossbow(NAMESPACE, "aether_heavy_crossbow", Equipment.Tier.TIER_4, ArcherItemTags.REPAIRS_AMBROSIUM).loot(-1, "aether"));
        }

        Weapon.register(meleeConfig, meleeEntries, Group.KEY);
        RangedWeapon.register(rangedConfig, rangedEntries, Group.KEY);
    }
}
