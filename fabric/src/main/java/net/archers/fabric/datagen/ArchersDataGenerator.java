package net.archers.fabric.datagen;

import net.archers.ArchersMod;
import net.archers.content.ArcherSounds;
import net.archers.content.ArcherSpells;
import net.archers.effect.ArcherEffects;
import net.archers.entity.ArcherEntities;
import net.archers.item.ArcherArmors;
import net.archers.item.ArcherItemTags;
import net.archers.item.ArcherWeapons;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.spell_engine.api.datagen.NamespacedLangGenerator;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.datagen.WeaponAttributeGenerator;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellTags;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArchersDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SoundGen::new);
        pack.addProvider(SpellGen::new);
        pack.addProvider(SpellTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(UnsmeltGenerator::new);
        pack.addProvider(ArcherRecipes::new);
        pack.addProvider(WeaponGen::new);
        pack.addProvider(ArchersAdvancements::new);
        pack.addProvider(LangGen::new);
    }

    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            generateWeaponTags(ArcherWeapons.meleeEntries);
            var bowEntries = ArcherWeapons.rangedEntries.stream().map(entry ->
                    new RPGSeriesDataGen.BowEntry(entry.id(), entry.category, entry.lootProperties)
            ).toList();
            generateBowTags(bowEntries);
            generateArmorTags(ArcherArmors.entries, RPGSeriesItemTags.ArmorMetaType.ARCHERY);

            // Anvil repair tags (`minecraft:repairable`), one per material
            for (var repair: ArcherItemTags.REPAIR_TAGS) {
                var tag = builder(repair.tag());
                repair.required().forEach(id -> tag.add(ResourceKey.create(Registries.ITEM, id)));
                repair.optional().forEach(id -> tag.addOptional(ResourceKey.create(Registries.ITEM, id)));
            }
        }
    }

    public static class SpellGen extends SpellGenerator {
        public SpellGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: ArcherSpells.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }

    public static class SpellTagGenerator extends FabricTagProvider<Spell> {
        public SpellTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, SpellRegistry.KEY, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            var namespace = ArchersMod.ID;
            // 1.21.6 split the tag-provider API: `getOrCreateTagBuilder` → key-based `builder(TagKey)`
            // (values are `RegistryKey`s) vs `valueLookupBuilder` for registry-object providers.
            var treasureTagBuilder = builder(SpellTags.TREASURE);
            var processedBooks = new HashSet<ArcherSpells.Book>();
            ArcherSpells.entries.forEach(entry -> {
                if (entry.book() != null) {
                    var bookTagKey = SpellTags.spellBook(namespace, entry.book().toString().toLowerCase());
                    builder(bookTagKey).addOptional(ResourceKey.create(SpellRegistry.KEY, entry.id()));
                    var scrollTagKey = SpellTags.spellScroll(namespace, entry.book().toString().toLowerCase());
                    builder(scrollTagKey).addOptional(ResourceKey.create(SpellRegistry.KEY, entry.id()));
                    if (processedBooks.add(entry.book())) {
                        treasureTagBuilder.addOptionalTag(scrollTagKey);
                    }
                }
            });
        }
    }

    public static class SoundGen extends SimpleSoundGeneratorV2 {
        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(ArchersMod.ID,
                            ArcherSounds.entries.stream()
                                    .map(entry -> SoundEntry.withVariants(entry.id().getPath(), entry.variants()))
                                    .toList()
                    )
            );
        }
    }

    public static class UnsmeltGenerator extends FabricRecipeProvider {
        public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        public static int UNSMELT_TIME = 300;

        @Override
        public String getName() {
            return "Archer Unsmelting Recipes";
        }

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
            disassembleArmor(output, ArcherArmors.archerArmorSet_T1, Items.LEATHER);
            disassembleArmor(output, ArcherArmors.archerArmorSet_T2, Items.TURTLE_SCUTE);
            disassembleArmor(output, ArcherArmors.archerArmorSet_T3, Items.NETHERITE_SCRAP);

            disassemble(output,
                    ArcherWeapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("flint"))
                            .map(entry -> (ItemLike) entry.item()).toList(),
                    Items.FLINT);
            disassemble(output,
                    ArcherWeapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("gold"))
                            .map(entry -> (ItemLike) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(output,
                    ArcherWeapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("iron"))
                            .map(entry -> (ItemLike) entry.item()).toList(),
                    Items.IRON_NUGGET);
//            disassemble(exporter,
//                    Weapons.meleeEntries.stream()
//                            .filter(entry -> entry.id().getPath().contains("diamond"))
//                            .map(entry -> (ItemConvertible) entry.item()).toList(),
//                    Items.DIAMOND);
            disassemble(output,
                    ArcherWeapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemLike) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);

            disassemble(output,
                    List.of(ArcherWeapons.mechanic_shortbow.item(), ArcherWeapons.rapid_crossbow.item()),
                    Items.REDSTONE);
            disassemble(output,
                    List.of(ArcherWeapons.royal_longbow.item()),
                    Items.GOLD_NUGGET);
            disassemble(output,
                    List.of(ArcherWeapons.heavy_crossbow.item()),
                    Items.IRON_NUGGET);
            disassemble(output,
                    ArcherWeapons.rangedEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemLike) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);
        }

        @SuppressWarnings("unchecked")
        private void disassembleArmor(RecipeOutput exporter, Armor.Set armorSet, Item output) {
            disassemble(exporter, (List<ItemLike>) (List<?>) armorSet.pieces(), output);
        }

        private void disassemble(RecipeOutput exporter, List<ItemLike> items, Item output) {
            oreSmelting(
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            oreBlasting(
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }
        }
    }

    public static class WeaponGen extends WeaponAttributeGenerator {
        public WeaponGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateWeaponAttributes(Builder builder) {
            ArcherWeapons.meleeEntries.forEach(entry -> {
                if (entry.weaponAttributesPreset != null && !entry.weaponAttributesPreset.isEmpty()) {
                    builder.entries.add(new Entry(entry.id(), entry.weaponAttributesPreset));
                }
            });
            ArcherWeapons.rangedEntries.forEach(entry -> {
                if (entry.weaponAttributesPreset != null && !entry.weaponAttributesPreset.isEmpty()) {
                    builder.entries.add(new Entry(entry.id(), entry.weaponAttributesPreset));
                }
            });
        }
    }

    /**
     * Generates the {@code en_us.json} language file from the in-code content definitions
     * (spells, status effects, weapons, armor, spell books) plus the advancement tree and a number of
     * ad-hoc strings (creative tab, quivers, auto-fire hook, workbench, villager) that have no dedicated
     * content entry.
     */
    public static class LangGen extends NamespacedLangGenerator {
        public LangGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup, ArchersMod.ID);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider registryLookup, FabricLanguageProvider.TranslationBuilder builder) {
            var namespace = ArchersMod.ID;

            // Creative tab
            builder.add("itemGroup." + namespace + ".general", "Archers");

            // Spell books & scrolls (one generated item per book)
            for (var book : ArcherSpells.Book.values()) {
                var key = book.name().toLowerCase();
                builder.add("item." + namespace + ".spell_book/" + key, book.bookName);
                builder.add("item." + namespace + ".spell_scroll/" + key, book.scrollName);
                builder.add("item." + namespace + ".spell_book/" + key + ".spell_binding.description", book.bindingDescription);
            }

            // Spells (only those given a display name in code)
            for (var entry : ArcherSpells.entries) {
                if (entry.title() == null || entry.title().isEmpty()) {
                    continue;
                }
                var path = entry.id().getPath();
                builder.add("spell." + namespace + "." + path + ".name", entry.title());
                builder.add("spell." + namespace + "." + path + ".description", entry.description());
            }

            // Status effects
            for (var entry : ArcherEffects.entries) {
                var path = entry.id.getPath();
                builder.add("effect." + namespace + "." + path, entry.title);
                builder.add("effect." + namespace + "." + path + ".description", entry.description);
            }

            // Weapons (melee spears + ranged bows/crossbows) — code-sourced display names
            ArcherWeapons.meleeEntries.forEach(entry -> addItemName(builder, entry.id(), entry.translatedName()));
            ArcherWeapons.rangedEntries.forEach(entry -> addItemName(builder, entry.id(), entry.translatedName()));
            // Conditional weapons are only registered when their host mod is present, so they are absent
            // from the weapon lists at data-gen time. Their names are provided directly.
            builder.add("item." + namespace + ".aeternium_spear", "Aeternium Spear");
            builder.add("item." + namespace + ".ruby_spear", "Ruby Spear");
            builder.add("item." + namespace + ".aether_spear", "Holy Spear");
            builder.add("item." + namespace + ".crystal_shortbow", "Crystal Shortbow");
            builder.add("item." + namespace + ".crystal_longbow", "Crystal Longbow");
            builder.add("item." + namespace + ".ruby_rapid_crossbow", "Ruby Rapid Crossbow");
            builder.add("item." + namespace + ".ruby_heavy_crossbow", "Ruby Heavy Crossbow");
            builder.add("item." + namespace + ".aether_longbow", "Silver Bow of the Acropolis");
            builder.add("item." + namespace + ".aether_rapid_crossbow", "Sky Crossbow");
            builder.add("item." + namespace + ".aether_heavy_crossbow", "Valkyrie Ballista");

            // Armor sets (per piece)
            for (var entry : ArcherArmors.entries) {
                var set = entry.armorSet();
                addItemName(builder, set.idOf(set.head), set.headTranslation);
                addItemName(builder, set.idOf(set.chest), set.chestTranslation);
                addItemName(builder, set.idOf(set.legs), set.legsTranslation);
                addItemName(builder, set.idOf(set.feet), set.feetTranslation);
            }

            // Quivers, auto-fire hook and workbench (miscellaneous items with no content-entry abstraction)
            builder.add("item." + namespace + ".small_quiver", "Quiver");
            builder.add("item." + namespace + ".medium_quiver", "Hunting Quiver");
            builder.add("item." + namespace + ".large_quiver", "Battle Quiver");
            builder.add("item." + namespace + ".quiver.hint", "Provides arrows for archery, when equipped.");
            builder.add("item." + namespace + ".quiver.empty.description", "Can hold mixed stacks of arrows");
            builder.add("item." + namespace + ".auto_fire_hook", "Auto-Fire Hook");
            builder.add("item." + namespace + ".auto_fire_hook.description_1", "Automatically releases charged arrow.");
            builder.add("item." + namespace + ".auto_fire_hook.description_2", "Can be applied to ranged weapons, on an Anvil.");
            builder.add("block." + namespace + ".archers_workbench", "Archery Artisan Table");
            builder.add("block." + namespace + ".archers_workbench.hint", "Workbench for Archery Artisan Villagers.");

            // Ranged Weapon API tooltip (this key lives in the ranged_weapon namespace)
            builder.add("item.ranged_weapon.pull_time", "%1$s sec Pull Time");

            // Custom entities — code-sourced display names (paired with the type in ArcherEntities.Entry)
            for (var entry : ArcherEntities.entries) {
                builder.add("entity." + namespace + "." + entry.id.getPath(), entry.name);
            }

            // Archery Artisan villager (several key formats are referenced across versions)
            builder.add("entity.minecraft.villager.archery_artisan", "Archery Artisan");
            builder.add("entity.minecraft.villager." + namespace + ".archery_artisan", "Archery Artisan");
            builder.add("entity.minecraft.villager." + namespace + ":archery_artisan", "Archery Artisan");

            // Advancements (generated alongside the rpg_series advancement JSONs)
            for (var advancement : ArchersAdvancements.entries()) {
                builder.add(advancement.titleKey(), advancement.title());
                builder.add(advancement.descriptionKey(), advancement.description());
            }
            // Advancement whose definition is provided elsewhere in the RPG Series, but whose
            // translation historically ships with Archers.
            builder.add("advancements.rpg_series.obtain_arrow.title", "Path of Archer");
            builder.add("advancements.rpg_series.obtain_arrow.description", "Obtain an Arrow");
        }

        private static void addItemName(FabricLanguageProvider.TranslationBuilder builder, Identifier id, String name) {
            if (name == null || name.isEmpty()) {
                return;
            }
            builder.add("item." + id.getNamespace() + "." + id.getPath(), name);
        }
    }
}
