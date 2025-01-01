package net.archers.item;

import net.archers.ArchersMod;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.Tiers;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.spell_engine.api.item.weapon.Weapon;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Weapons {
    public static final ArrayList<RangedEntry> rangedEntries = new ArrayList<>();
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();

    public interface RangedFactory {
        Item create(Item.Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier);
    }

    public static final class RangedEntry {
        private final Identifier id;
        private final RangedFactory factory;
        private final RangedConfig defaults;
        private final Supplier<Ingredient> repairIngredientSupplier;
        private final int durability;

        public Item item;

        public RangedEntry(Identifier id, RangedFactory factory, RangedConfig defaults, Supplier<Ingredient> repairIngredientSupplier, int durability) {
            this.id = id;
            this.factory = factory;
            this.defaults = defaults;
            this.repairIngredientSupplier = repairIngredientSupplier;
            this.durability = durability;
        }

        public Identifier id() {
            return id;
        }

        public RangedFactory factory() {
            return factory;
        }

        public RangedConfig defaults() {
            return defaults;
        }

        public Supplier<Ingredient> repairIngredientSupplier() {
            return repairIngredientSupplier;
        }

        public int durability() {
            return durability;
        }

        public Item create(Item.Settings settings) {
            this.item = factory.create(
                    settings.maxDamage(durability),
                    defaults,
                    repairIngredientSupplier
            );
            return this.item;
        }

        public Item item() {
            return item;
        }
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
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

    /**
     * MELEE WEAPONS
     */

    private static Weapon.Entry addMelee(String name, Weapon.CustomMaterial material, Weapon.Factory factory, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(ArchersMod.ID, name, material, factory, defaults, null);
        meleeEntries.add(entry);
        return entry;
    }

    private static Weapon.Entry spear(String name, Weapon.CustomMaterial material, float damage) {
        return addMelee(name, material, SpellWeaponItem::new, new ItemConfig.Weapon(damage, -2.6F));
    }

    public static final Weapon.Entry flint_spear = spear("flint_spear",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.ofItems(Items.FLINT)), 4F);
    public static final Weapon.Entry iron_spear = spear("iron_spear",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 5F);
    public static final Weapon.Entry golden_spear = spear("golden_spear",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 3F);
    public static final Weapon.Entry diamond_spear = spear("diamond_spear",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 6F);
    public static final Weapon.Entry netherite_spear = spear("netherite_spear",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 7F);

    /**
     * RANGED WEAPONS
     */

    private static RangedEntry bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var entry = new RangedEntry(Identifier.of(ArchersMod.ID, name), CustomBow::new, defaults, repairIngredientSupplier, durability);
        rangedEntries.add(entry);
        return entry;
    }

    private static RangedEntry crossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var entry = new RangedEntry(Identifier.of(ArchersMod.ID, name), CustomCrossbow::new, defaults, repairIngredientSupplier, durability);
        rangedEntries.add(entry);
        return entry;
    }

    private static final int durabilityTier0 = 384;
    private static final int durabilityTier1 = 465; // Matches Vanilla Crossbow durability
    private static final int durabilityTier2 = ToolMaterials.DIAMOND.getDurability();
    private static final int durabilityTier3 = ToolMaterials.NETHERITE.getDurability();

    private static final float pullTime_shortBow = 0.8F - 1F;
    private static final float pullTime_longBow = 1.5F - 1F;
    private static final float pullTime_rapidCrossbow = 0;
    private static final float pullTime_heavyCrossbow = 1.75F - 1F;
    private static final float velocity_shortBow = 0F;
    private static final float velocity_longBow = 0.75F;
    private static final float velocity_rapidCrossbow = 0F;
    private static final float velocity_heavyCrossbow = 0.5F;

    /**
     * DPS Tiers
     * T0: 6 DPS (from Vanilla Bow, 6 / 1)
     * T1: 7.2 DPS (from Vanilla CrossBow, 9 / 1.25)
     *  T0 -> T1  20% increase
     * T2: 8.4 DPS
     * T3: 9.6 DPS
     *
     * Target Item Damage = DPS * Pull Time (for example:  7.2 * (25/20) )
     */

    /**
     * BOWS
     */

    public static RangedEntry composite_longbow = bow("composite_longbow", durabilityTier1,
            () -> Ingredient.ofItems(Items.BONE),
            new RangedConfig(8, pullTime_longBow, velocity_longBow));

    public static RangedEntry mechanic_shortbow = bow("mechanic_shortbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(8F, pullTime_shortBow,  velocity_shortBow));

    public static RangedEntry royal_longbow = bow("royal_longbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.GOLD_INGOT),
            new RangedConfig(10, pullTime_longBow, velocity_longBow));

    public static RangedEntry netherite_shortbow = bow("netherite_shortbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
            new RangedConfig(9, pullTime_shortBow, velocity_shortBow));

    public static RangedEntry netherite_longbow = bow("netherite_longbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
            new RangedConfig(12, pullTime_longBow, velocity_longBow));


    /**
     * CROSSBOWS
     */

    public static RangedEntry rapid_crossbow = crossbow("rapid_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(8.5F, pullTime_rapidCrossbow, velocity_rapidCrossbow));

    public static RangedEntry heavy_crossbow = crossbow("heavy_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.DIAMOND),
            new RangedConfig(13, pullTime_heavyCrossbow,  velocity_heavyCrossbow));

    public static RangedEntry netherite_rapid_crossbow = crossbow("netherite_rapid_crossbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
            new RangedConfig(9.5F, pullTime_rapidCrossbow, velocity_rapidCrossbow));

    public static RangedEntry netherite_heavy_crossbow = crossbow("netherite_heavy_crossbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
            new RangedConfig(15, pullTime_heavyCrossbow, velocity_heavyCrossbow));


    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, ItemConfig.Weapon> meleeConfig) {
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_END)) {
            var aeterniumRepair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            var crystalRepair = ingredient("betterend:crystal_shards", FabricLoader.getInstance().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            spear("aeternium_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, aeterniumRepair), 8F);
            bow("crystal_shortbow", durabilityTier3, crystalRepair,
                    new RangedConfig(10F, pullTime_shortBow, velocity_shortBow));
            bow("crystal_longbow", durabilityTier3, crystalRepair,
                    new RangedConfig(13.5F, pullTime_longBow, velocity_longBow));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_NETHER)) {
            var rubyRepair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            spear("ruby_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, rubyRepair), 8F);
            crossbow("ruby_rapid_crossbow", durabilityTier3, rubyRepair,
                    new RangedConfig(10.5F, pullTime_rapidCrossbow, velocity_rapidCrossbow));
            crossbow("ruby_heavy_crossbow", durabilityTier3, rubyRepair,
                    new RangedConfig(17, pullTime_heavyCrossbow, velocity_heavyCrossbow));
        }
        if (ArchersMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(AETHER)) {
            var aetherRepair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            spear("aether_spear",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, aetherRepair), 8F);
            bow("aether_longbow", durabilityTier3, aetherRepair,
                    new RangedConfig(13.5F, pullTime_longBow, velocity_rapidCrossbow));
            crossbow("aether_rapid_crossbow", durabilityTier3, aetherRepair,
                    new RangedConfig(10.5F, pullTime_rapidCrossbow, velocity_rapidCrossbow));
            crossbow("aether_heavy_crossbow", durabilityTier3, aetherRepair,
                    new RangedConfig(17, pullTime_heavyCrossbow,velocity_heavyCrossbow));
        }

        var netheriteTier = Tiers.unsafe("netherite");
        Weapon.register(meleeConfig, meleeEntries, Group.KEY);

        for (var entry: rangedEntries) {
            var config = rangedConfig.get(entry.id.toString());
            if (config == null) {
                config = entry.defaults;
                rangedConfig.put(entry.id.toString(), config);
            }
            var settings = new Item.Settings();
            var tier = Tiers.unsafe(entry.id());
            if (tier >= netheriteTier) {
                settings.fireproof();
            }
            var item = entry.create(settings);
            Registry.register(Registries.ITEM, entry.id, item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: rangedEntries) {
                content.add(entry.item);
            }
        });
    }
}
