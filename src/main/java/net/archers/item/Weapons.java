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

    /**
     * BOWS
     */

    public static Entry composite_longbow = bow("composite_longbow", durabilityTier1,
            () -> Ingredient.ofItems(Items.BONE),
            new RangedConfig(30, 7, null));

    public static Entry mechanic_shortbow = bow("mechanic_shortbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(10, 5, null));

    public static Entry royal_longbow = bow("royal_longbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.GOLD_INGOT),
            new RangedConfig(30, 8, null));

    /**
     * CROSSBOWS
     */

    public static Entry rapid_crossbow = crossbow("rapid_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.REDSTONE),
            new RangedConfig(10, 10, null));

    public static Entry heavy_crossbow = crossbow("heavy_crossbow", durabilityTier2,
            () -> Ingredient.ofItems(Items.DIAMOND),
            new RangedConfig(30, 11, null));

    public static void register() {
        for (var entry: all) {
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: all) {
                content.add(entry.item);
            }
        });
    }
}
