package net.archers.datagen;

import net.archers.block.ArcherBlocks;
import net.archers.item.ArcherArmors;
import net.archers.item.ArcherWeapons;
import net.archers.item.misc.Misc;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.item.Weapon;

import java.util.concurrent.CompletableFuture;

/**
 * Generates all crafting recipes for the Archers mod using Fabric's built-in API.
 * Conditional recipes (BetterEnd/BetterNether) are kept as hand-written JSONs.
 */
public class ArcherRecipes extends FabricRecipeProvider {

    public ArcherRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        generateSpearRecipes(exporter);
        generateBowRecipes(exporter);
        generateCrossbowRecipes(exporter);
        generateArmorRecipes(exporter);
        generateOtherRecipes(exporter);
        generateNetheriteUpgrades(exporter);
    }

    // ========================================
    // SPEAR RECIPES
    // ========================================

    private void generateSpearRecipes(RecipeExporter exporter) {
        spear(exporter, ArcherWeapons.flint_spear, Items.FLINT);
        spear(exporter, ArcherWeapons.iron_spear, Items.IRON_INGOT);
        spear(exporter, ArcherWeapons.golden_spear, Items.GOLD_INGOT);
        spear(exporter, ArcherWeapons.diamond_spear, Items.DIAMOND);
    }

    /**
     * Generate spear recipe with standard pattern
     */
    private void spear(RecipeExporter exporter, Weapon.Entry spearEntry, Item tipMaterial) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, spearEntry.item())
                .pattern("  P")
                .pattern(" # ")
                .pattern("#  ")
                .input('P', tipMaterial)
                .input('#', Items.STICK)
                .criterion(hasItem(tipMaterial), conditionsFromItem(tipMaterial))
                .offerTo(exporter);
    }

    // ========================================
    // BOW RECIPES
    // ========================================

    private void generateBowRecipes(RecipeExporter exporter) {
        // Composite Longbow - bone + stick + string
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArcherWeapons.composite_longbow.item())
                .pattern(" #X")
                .pattern("B X")
                .pattern(" #X")
                .input('#', Items.STICK)
                .input('B', Items.BONE)
                .input('X', Items.STRING)
                .criterion(hasItem(Items.BONE), conditionsFromItem(Items.BONE))
                .offerTo(exporter);

        // Mechanic Shortbow - iron + redstone + string
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArcherWeapons.mechanic_shortbow.item())
                .pattern(" IX")
                .pattern("R X")
                .pattern(" IX")
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .input('X', Items.STRING)
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter);

        // Royal Longbow - gold + diamond + string
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArcherWeapons.royal_longbow.item())
                .pattern(" GX")
                .pattern("D X")
                .pattern(" GX")
                .input('G', Items.GOLD_INGOT)
                .input('D', Items.DIAMOND)
                .input('X', Items.STRING)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // CROSSBOW RECIPES
    // ========================================

    private void generateCrossbowRecipes(RecipeExporter exporter) {
        // Rapid Crossbow - iron + redstone + string + tripwire_hook
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArcherWeapons.rapid_crossbow.item())
                .pattern("IRI")
                .pattern("XTX")
                .pattern(" I ")
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .input('X', Items.STRING)
                .input('T', Items.TRIPWIRE_HOOK)
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter);

        // Heavy Crossbow - iron + diamond + string + tripwire_hook
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArcherWeapons.heavy_crossbow.item())
                .pattern("IDI")
                .pattern("XTX")
                .pattern(" I ")
                .input('I', Items.IRON_INGOT)
                .input('D', Items.DIAMOND)
                .input('X', Items.STRING)
                .input('T', Items.TRIPWIRE_HOOK)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // ARMOR RECIPES
    // ========================================

    private void generateArmorRecipes(RecipeExporter exporter) {
        // Archer Armor (T1) - leather + chain
        generateArcherArmorSet(exporter, ArcherArmors.archerArmorSet_T1, Items.LEATHER, Items.CHAIN);

        // Ranger Armor (T2) - leather + rabbit_hide + turtle_scute
        generateRangerArmorSet(exporter, ArcherArmors.archerArmorSet_T2, Items.LEATHER, Items.RABBIT_HIDE, Items.TURTLE_SCUTE);
    }

    /**
     * Generate Archer armor set (T1 - simple pattern with leather + chain)
     */
    private void generateArcherArmorSet(RecipeExporter exporter, Armor.Set armorSet, Item leather, Item chain) {
        // Helmet - pattern: "LLL" / "C C"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("LLL")
                .pattern("C C")
                .input('L', leather)
                .input('C', chain)
                .criterion(hasItem(chain), conditionsFromItem(chain))
                .offerTo(exporter);

        // Chestplate - pattern: "C C" / "LLL" / "LLL"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("C C")
                .pattern("LLL")
                .pattern("LLL")
                .input('L', leather)
                .input('C', chain)
                .criterion(hasItem(chain), conditionsFromItem(chain))
                .offerTo(exporter);

        // Leggings - pattern: "CCC" / "L L" / "L L"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("CCC")
                .pattern("L L")
                .pattern("L L")
                .input('L', leather)
                .input('C', chain)
                .criterion(hasItem(chain), conditionsFromItem(chain))
                .offerTo(exporter);

        // Boots - pattern: "C C" / "L L"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("C C")
                .pattern("L L")
                .input('L', leather)
                .input('C', chain)
                .criterion(hasItem(chain), conditionsFromItem(chain))
                .offerTo(exporter);
    }

    /**
     * Generate Ranger armor set (T2 - uses leather, rabbit_hide, and turtle_scute)
     */
    private void generateRangerArmorSet(RecipeExporter exporter, Armor.Set armorSet, Item leather, Item rabbitHide, Item turtleScute) {
        // Helmet - pattern: "SRS" / "L L"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("SRS")
                .pattern("L L")
                .input('S', turtleScute)
                .input('R', rabbitHide)
                .input('L', leather)
                .criterion(hasItem(turtleScute), conditionsFromItem(turtleScute))
                .offerTo(exporter);

        // Chestplate - pattern: "R R" / "LSL" / "LLL"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("R R")
                .pattern("LSL")
                .pattern("LLL")
                .input('L', leather)
                .input('R', rabbitHide)
                .input('S', turtleScute)
                .criterion(hasItem(turtleScute), conditionsFromItem(turtleScute))
                .offerTo(exporter);

        // Leggings - pattern: "SRS" / "L L" / "L L"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("SRS")
                .pattern("L L")
                .pattern("L L")
                .input('L', leather)
                .input('R', rabbitHide)
                .input('S', turtleScute)
                .criterion(hasItem(turtleScute), conditionsFromItem(turtleScute))
                .offerTo(exporter);

        // Boots - pattern: "R R" / "L L"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("R R")
                .pattern("L L")
                .input('L', leather)
                .input('R', rabbitHide)
                .criterion(hasItem(rabbitHide), conditionsFromItem(rabbitHide))
                .offerTo(exporter);
    }

    // ========================================
    // OTHER RECIPES
    // ========================================

    private void generateOtherRecipes(RecipeExporter exporter) {
        // Archers Workbench
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ArcherBlocks.WORKBENCH.block())
                .pattern("SAL")
                .pattern("###")
                .input('S', Items.STRING)
                .input('A', Items.ARROW)
                .input('L', Items.LEATHER)
                .input('#', ItemTags.PLANKS)
                .criterion(hasItem(Items.ARROW), conditionsFromItem(Items.ARROW))
                .offerTo(exporter);

        // Auto Fire Hook
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Misc.autoFireHook.item())
                .pattern("C  ")
                .pattern("C I")
                .pattern(" R ")
                .input('C', Items.COPPER_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter);
    }

    // ========================================
    // NETHERITE UPGRADE RECIPES
    // ========================================

    private void generateNetheriteUpgrades(RecipeExporter exporter) {
        // Weapon upgrades
        offerNetheriteUpgradeRecipe(exporter, ArcherWeapons.diamond_spear.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_spear.item());
        offerNetheriteUpgradeRecipe(exporter, ArcherWeapons.royal_longbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_longbow.item());
        offerNetheriteUpgradeRecipe(exporter, ArcherWeapons.mechanic_shortbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_shortbow.item());
        offerNetheriteUpgradeRecipe(exporter, ArcherWeapons.rapid_crossbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_rapid_crossbow.item());
        offerNetheriteUpgradeRecipe(exporter, ArcherWeapons.heavy_crossbow.item(), RecipeCategory.COMBAT, ArcherWeapons.netherite_heavy_crossbow.item());

        // Ranger armor upgrades (T2 -> T3)
        offerNetheriteUpgradeRecipe(exporter, ArcherArmors.archerArmorSet_T2.head, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.head);
        offerNetheriteUpgradeRecipe(exporter, ArcherArmors.archerArmorSet_T2.chest, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.chest);
        offerNetheriteUpgradeRecipe(exporter, ArcherArmors.archerArmorSet_T2.legs, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.legs);
        offerNetheriteUpgradeRecipe(exporter, ArcherArmors.archerArmorSet_T2.feet, RecipeCategory.COMBAT, ArcherArmors.archerArmorSet_T3.feet);
    }

    @Override
    public String getName() {
        return "Archer Crafting Recipes";
    }
}
