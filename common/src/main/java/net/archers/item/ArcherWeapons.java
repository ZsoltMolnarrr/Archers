package net.archers.item;

import net.archers.ArchersMod;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.RangedWeapon;
import net.spell_engine.rpg_series.item.RangedWeapons;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.rpg_series.item.Weapons;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

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

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = new Identifier(idString);
        if (requirement) {
            return () -> Ingredient.ofItems(fallback);
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String AETHER = "aether";

    // MARK: Spears

    public static final Weapon.Entry flint_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "flint_spear", Equipment.Tier.TIER_0, () -> Ingredient.ofItems(Items.FLINT))
            .translatedName("Flint Spear"));
    public static final Weapon.Entry iron_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "iron_spear", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT))
            .translatedName("Iron Spear"));
    public static final Weapon.Entry golden_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "golden_spear", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .loot(Equipment.LootProperties.of("golden_weapon"))
            .translatedName("Golden Spear"));
    public static final Weapon.Entry diamond_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "diamond_spear", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .translatedName("Diamond Spear"));
    public static final Weapon.Entry netherite_spear = addMelee(Weapons.spearWithSkill(
            NAMESPACE, "netherite_spear", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Spear"));

    // MARK: Bows

    public static final RangedWeapon.Entry composite_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "composite_longbow", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.BONE))
            .translatedName("Composite Longbow"));
    public static final RangedWeapon.Entry mechanic_shortbow = addRanged(RangedWeapons.shortBow(
            NAMESPACE, "mechanic_shortbow", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.REDSTONE))
            .translatedName("Mechanical Shortbow"));
    public static final RangedWeapon.Entry royal_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "royal_longbow", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .translatedName("Royal Longbow"));
    public static final RangedWeapon.Entry netherite_shortbow = addRanged(RangedWeapons.shortBow(
            NAMESPACE, "netherite_shortbow", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Shortbow"));
    public static final RangedWeapon.Entry netherite_longbow = addRanged(RangedWeapons.longBow(
            NAMESPACE, "netherite_longbow", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Longbow"));

    // MARK: Crossbows

    public static final RangedWeapon.Entry rapid_crossbow = addRanged(RangedWeapons.rapidCrossbow(
            NAMESPACE, "rapid_crossbow", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.REDSTONE))
            .translatedName("Rapid Crossbow"));
    public static final RangedWeapon.Entry heavy_crossbow = addRanged(RangedWeapons.heavyCrossbow(
            NAMESPACE, "heavy_crossbow", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .translatedName("Heavy Crossbow"));
    public static final RangedWeapon.Entry netherite_rapid_crossbow = addRanged(RangedWeapons.rapidCrossbow(
            NAMESPACE, "netherite_rapid_crossbow", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Rapid Crossbow"));
    public static final RangedWeapon.Entry netherite_heavy_crossbow = addRanged(RangedWeapons.heavyCrossbow(
            NAMESPACE, "netherite_heavy_crossbow", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Heavy Crossbow"));

    // MARK: Register

    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, WeaponConfig> meleeConfig) {
        itemsToRegister(rangedConfig, meleeConfig)
                .forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
    }

    /// Adds the mod-gated entries, then builds every weapon item, keyed by the id it registers under.
    /// Creation only — a loader that registers items itself (Forge) iterates this instead of calling
    /// {@link #register}. Must run inside the `ITEM` registration window: `Item`'s constructor takes an
    /// intrusive registry holder.
    public static Map<Identifier, Item> itemsToRegister(Map<String, RangedConfig> rangedConfig, Map<String, WeaponConfig> meleeConfig) {
        addConditionalEntries();
        var items = new LinkedHashMap<Identifier, Item>();
        // Melee before ranged — this is also the order they take in the creative tab, because both
        // helpers install their item-group listener as they build.
        items.putAll(Weapon.itemsToRegister(meleeConfig, meleeEntries, Group.KEY));
        items.putAll(RangedWeapon.itemsToRegister(rangedConfig, rangedEntries, Group.KEY));
        return items;
    }

    private static boolean conditionalEntriesAdded = false;

    private static void addConditionalEntries() {
        if (conditionalEntriesAdded) { return; }
        conditionalEntriesAdded = true;
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            var aeterniumRepair = ingredient("betterend:aeternium_ingot", Platform.util().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            var crystalRepair = ingredient("betterend:crystal_shards", Platform.util().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            addMelee(Weapons.spearWithSkill(NAMESPACE, "aeternium_spear", Equipment.Tier.TIER_4, aeterniumRepair));
            addRanged(RangedWeapons.shortBow(NAMESPACE, "crystal_shortbow", Equipment.Tier.TIER_4, crystalRepair));
            addRanged(RangedWeapons.longBow(NAMESPACE, "crystal_longbow", Equipment.Tier.TIER_4, crystalRepair));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            var rubyRepair = ingredient("betternether:nether_ruby", Platform.util().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            addMelee(Weapons.spearWithSkill(NAMESPACE, "ruby_spear", Equipment.Tier.TIER_4, rubyRepair));
            addRanged(RangedWeapons.rapidCrossbow(NAMESPACE, "ruby_rapid_crossbow", Equipment.Tier.TIER_4, rubyRepair));
            addRanged(RangedWeapons.heavyCrossbow(NAMESPACE, "ruby_heavy_crossbow", Equipment.Tier.TIER_4, rubyRepair));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            var aetherRepair = ingredient("aether:ambrosium_shard", Platform.util().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            addMelee(Weapons.spearWithSkill(NAMESPACE, "aether_spear", Equipment.Tier.TIER_4, aetherRepair)
                    .loot(Equipment.LootProperties.of("aether")));
            addRanged(RangedWeapons.longBow(NAMESPACE, "aether_longbow", Equipment.Tier.TIER_4, aetherRepair).loot(-1, "aether"));
            addRanged(RangedWeapons.rapidCrossbow(NAMESPACE, "aether_rapid_crossbow", Equipment.Tier.TIER_4, aetherRepair).loot(-1, "aether"));
            addRanged(RangedWeapons.heavyCrossbow(NAMESPACE, "aether_heavy_crossbow", Equipment.Tier.TIER_4, aetherRepair).loot(-1, "aether"));
        }
    }
}
