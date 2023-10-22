package net.archers.item;

import net.archers.ArchersMod;
import net.archers.config.RangedConfig;
import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomCrossbow;
import net.archers.item.weapon.CustomRangedWeaponProperties;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Weapons {
    public static final ArrayList<Entry> all = new ArrayList<>();
    public record Entry(Identifier id, Item item, RangedConfig defaults) { }
    private static Entry add(Identifier id, Item item, RangedConfig defaults) {
        var entry = new Entry(id, item, defaults);
        all.add(entry);
        return entry;
    }

    private static Entry bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new CustomBow(settings, repairIngredientSupplier);
        ((CustomRangedWeaponProperties)item).configure(defaults);
        return add(new Identifier(ArchersMod.ID, name), item, defaults);
    }

    private static Entry crossbow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new CustomCrossbow(settings, repairIngredientSupplier);
        ((CustomRangedWeaponProperties)item).configure(defaults);
        return add(new Identifier(ArchersMod.ID, name), item, defaults);
    }

    private static final int durabilityTier0 = 384;
    private static final int durabilityTier1 = 465; // Matches Vanilla Crossbow durability
    private static final int durabilityTier2 = ToolMaterials.DIAMOND.getDurability();
    private static final int durabilityTier3 = ToolMaterials.NETHERITE.getDurability();

    private static final int pullTime_shortBow = 16;
    private static final int pullTime_longBow = 30;
    private static final int pullTime_rapidCrossbow = 20;
    private static final int pullTime_heavyCrossbow = 35;

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

    public static Entry composite_longbow = bow("composite_longbow", durabilityTier1,
            () -> Ingredient.ofItems(Items.BONE),
            new RangedConfig(pullTime_longBow, 9, null));

    public static Entry mechanic_shortbow = bow("mechanic_shortbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(pullTime_shortBow, 6.5F, null));

    public static Entry royal_longbow = bow("royal_longbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.GOLD_INGOT),
            new RangedConfig(pullTime_longBow, 12, null));

    public static Entry netherite_shortbow = bow("netherite_shortbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(pullTime_shortBow, 7, null));

    public static Entry netherite_longbow = bow("netherite_longbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.GOLD_INGOT),
            new RangedConfig(pullTime_longBow, 14, null));


    /**
     * CROSSBOWS
     */

    public static Entry rapid_crossbow = crossbow("rapid_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(pullTime_rapidCrossbow, 8.5F, null));

    public static Entry heavy_crossbow = crossbow("heavy_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.DIAMOND),
            new RangedConfig(pullTime_heavyCrossbow, 14, null));

    public static Entry netherite_rapid_crossbow = crossbow("netherite_rapid_crossbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(pullTime_rapidCrossbow, 9.5F, null));

    public static Entry netherite_heavy_crossbow = crossbow("netherite_heavy_crossbow", durabilityTier3,
            () -> Ingredient.ofItems(Items.DIAMOND),
            new RangedConfig(pullTime_heavyCrossbow, 16, null));


    public static void register(Map<String, RangedConfig> configs) {
        for (var entry: all) {
            var config = configs.get(entry.id.toString());
            if (config == null) {
                config = entry.defaults;
                configs.put(entry.id.toString(), config);
            }
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: all) {
                content.add(entry.item);
            }
        });
    }
}
