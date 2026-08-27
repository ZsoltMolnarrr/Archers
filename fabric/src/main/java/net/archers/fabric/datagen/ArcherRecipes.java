package net.archers.fabric.datagen;

import net.archers.block.ArcherBlocks;
import net.archers.item.ArcherArmors;
import net.archers.item.ArcherWeapons;
import net.archers.item.misc.Misc;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.item.Weapon;

import java.util.concurrent.CompletableFuture;

/**
 * Generates all crafting recipes for the Archers mod using Fabric's built-in API.
 * Conditional recipes (BetterEnd/BetterNether) are kept as hand-written JSONs.
 */
public class ArcherRecipes extends FabricRecipeProvider {

    public ArcherRecipes(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /// Since 1.21.2 the recipe provider only *builds* a {@link RecipeGenerator}; the recipe-building
    /// helpers (`createShaped`, `hasItem`, `conditionsFromItem`, `offerNetheriteUpgradeRecipe`) are
    /// instance members of the generator, which also holds the exporter.
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
        return new Generator(registries, exporter);
    }

    private static class Generator extends RecipeProvider {
        Generator(HolderLookup.Provider registries, RecipeOutput exporter) {
            super(registries, exporter);
        }

    @Override
    public void buildRecipes() {
        generateSpearRecipes(output);
        generateBowRecipes(output);
        generateCrossbowRecipes(output);
        generateArmorRecipes(output);
        generateOtherRecipes(output);
        generateNetheriteUpgrades(output);
    }

    // ========================================
    // SPEAR RECIPES
    // ========================================

    private void generateSpearRecipes(RecipeOutput exporter) {
        spear(exporter, ArcherWeapons.flint_spear, Items.FLINT);
        spear(exporter, ArcherWeapons.iron_spear, Items.IRON_INGOT);
        spear(exporter, ArcherWeapons.golden_spear, Items.GOLD_INGOT);
        spear(exporter, ArcherWeapons.diamond_spear, Items.DIAMOND);
    }

    /**
     * Generate spear recipe with standard pattern
     */
    private void spear(RecipeOutput exporter, Weapon.Entry spearEntry, Item tipMaterial) {
        shaped(RecipeCategory.COMBAT, spearEntry.item())
                .pattern("  P")
                .pattern(" # ")
                .pattern("#  ")
                .define('P', tipMaterial)
                .define('#', Items.STICK)
                .unlockedBy(getHasName(tipMaterial), has(tipMaterial))
                .save(exporter);
    }

    // ========================================
    // BOW RECIPES
    // ========================================

    private void generateBowRecipes(RecipeOutput exporter) {
        // Composite Longbow - bone + stick + string
        shaped(RecipeCategory.COMBAT, ArcherWeapons.composite_longbow.item())
                .pattern(" #X")
                .pattern("B X")
                .pattern(" #X")
                .define('#', Items.STICK)
                .define('B', Items.BONE)
                .define('X', Items.STRING)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .save(exporter);

        // Mechanic Shortbow - iron + redstone + string
        shaped(RecipeCategory.COMBAT, ArcherWeapons.mechanic_shortbow.item())
                .pattern(" IX")
                .pattern("R X")
                .pattern(" IX")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('X', Items.STRING)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(exporter);

        // Royal Longbow - gold + diamond + string
        shaped(RecipeCategory.COMBAT, ArcherWeapons.royal_longbow.item())
                .pattern(" GX")
                .pattern("D X")
                .pattern(" GX")
                .define('G', Items.GOLD_INGOT)
                .define('D', Items.DIAMOND)
                .define('X', Items.STRING)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(exporter);
    }

    // ========================================
    // CROSSBOW RECIPES
    // ========================================

    private void generateCrossbowRecipes(RecipeOutput exporter) {
        // Rapid Crossbow - iron + redstone + string + tripwire_hook
        shaped(RecipeCategory.COMBAT, ArcherWeapons.rapid_crossbow.item())
                .pattern("IRI")
                .pattern("XTX")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('X', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(exporter);

        // Heavy Crossbow - iron + diamond + string + tripwire_hook
        shaped(RecipeCategory.COMBAT, ArcherWeapons.heavy_crossbow.item())
                .pattern("IDI")
                .pattern("XTX")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('X', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(exporter);
    }

    // ========================================
    // ARMOR RECIPES
    // ========================================

    private void generateArmorRecipes(RecipeOutput exporter) {
        // Archer Armor (T1) - leather + chain
        generateArcherArmorSet(exporter, ArcherArmors.archerArmorSet_T1, Items.LEATHER, Items.IRON_CHAIN);

        // Ranger Armor (T2) - leather + rabbit_hide + turtle_scute
        generateRangerArmorSet(exporter, ArcherArmors.archerArmorSet_T2, Items.LEATHER, Items.RABBIT_HIDE, Items.TURTLE_SCUTE);
    }

    /**
     * Generate Archer armor set (T1 - simple pattern with leather + chain)
     */
    private void generateArcherArmorSet(RecipeOutput exporter, Armor.Set armorSet, Item leather, Item chain) {
        // Helmet - pattern: "LLL" / "C C"
        shaped(RecipeCategory.COMBAT, armorSet.head)
                .pattern("LLL")
                .pattern("C C")
                .define('L', leather)
                .define('C', chain)
                .unlockedBy(getHasName(chain), has(chain))
                .save(exporter);

        // Chestplate - pattern: "C C" / "LLL" / "LLL"
        shaped(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("C C")
                .pattern("LLL")
                .pattern("LLL")
                .define('L', leather)
                .define('C', chain)
                .unlockedBy(getHasName(chain), has(chain))
                .save(exporter);

        // Leggings - pattern: "CCC" / "L L" / "L L"
        shaped(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("CCC")
                .pattern("L L")
                .pattern("L L")
                .define('L', leather)
                .define('C', chain)
                .unlockedBy(getHasName(chain), has(chain))
                .save(exporter);

        // Boots - pattern: "C C" / "L L"
        shaped(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("C C")
                .pattern("L L")
                .define('L', leather)
                .define('C', chain)
                .unlockedBy(getHasName(chain), has(chain))
                .save(exporter);
    }

    /**
     * Generate Ranger armor set (T2 - uses leather, rabbit_hide, and turtle_scute)
     */
    private void generateRangerArmorSet(RecipeOutput exporter, Armor.Set armorSet, Item leather, Item rabbitHide, Item turtleScute) {
        // Helmet - pattern: "SRS" / "L L"
        shaped(RecipeCategory.COMBAT, armorSet.head)
                .pattern("SRS")
                .pattern("L L")
                .define('S', turtleScute)
                .define('R', rabbitHide)
                .define('L', leather)
                .unlockedBy(getHasName(turtleScute), has(turtleScute))
                .save(exporter);

        // Chestplate - pattern: "R R" / "LSL" / "LLL"
        shaped(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("R R")
                .pattern("LSL")
                .pattern("LLL")
                .define('L', leather)
                .define('R', rabbitHide)
                .define('S', turtleScute)
                .unlockedBy(getHasName(turtleScute), has(turtleScute))
                .save(exporter);

        // Leggings - pattern: "SRS" / "L L" / "L L"
        shaped(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("SRS")
                .pattern("L L")
                .pattern("L L")
                .define('L', leather)
                .define('R', rabbitHide)
                .define('S', turtleScute)
                .unlockedBy(getHasName(turtleScute), has(turtleScute))
                .save(exporter);

        // Boots - pattern: "R R" / "L L"
        shaped(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("R R")
                .pattern("L L")
                .define('L', leather)
                .define('R', rabbitHide)
                .unlockedBy(getHasName(rabbitHide), has(rabbitHide))
                .save(exporter);
    }

    // ========================================
    // OTHER RECIPES
    // ========================================

    private void generateOtherRecipes(RecipeOutput exporter) {
        // Archers Workbench
        shaped(RecipeCategory.MISC, ArcherBlocks.WORKBENCH.block())
                .pattern("SAL")
                .pattern("###")
                .define('S', Items.STRING)
                .define('A', Items.ARROW)
                .define('L', Items.LEATHER)
                .define('#', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.ARROW), has(Items.ARROW))
                .save(exporter);

        // Auto Fire Hook
        shaped(RecipeCategory.COMBAT, Misc.autoFireHook.item())
                .pattern("C  ")
                .pattern("C I")
                .pattern(" R ")
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(exporter);
    }

    // ========================================
    // NETHERITE UPGRADE RECIPES
    // ========================================

    private void generateNetheriteUpgrades(RecipeOutput exporter) {
        // Weapon upgrades
        netheriteSmithing(ArcherWeapons.diamond_spear.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_spear.item());
        netheriteSmithing(ArcherWeapons.royal_longbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_longbow.item());
        netheriteSmithing(ArcherWeapons.mechanic_shortbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_shortbow.item());
        netheriteSmithing(ArcherWeapons.rapid_crossbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_rapid_crossbow.item());
        netheriteSmithing(ArcherWeapons.heavy_crossbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_heavy_crossbow.item());

        // Ranger armor upgrades (T2 -> T3)
        netheriteSmithing(ArcherArmors.archerArmorSet_T2.head, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.head);
        netheriteSmithing(ArcherArmors.archerArmorSet_T2.chest, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.chest);
        netheriteSmithing(ArcherArmors.archerArmorSet_T2.legs, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.legs);
        netheriteSmithing(ArcherArmors.archerArmorSet_T2.feet, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.feet);
    }

    }

    @Override
    public String getName() {
        return "Archer Crafting Recipes";
    }
}
