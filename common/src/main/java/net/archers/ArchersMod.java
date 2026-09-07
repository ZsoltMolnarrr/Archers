package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.config.TweaksConfig;
import net.archers.effect.ArcherEffects;
import net.archers.entity.ArcherEntities;
import net.archers.item.Group;
import net.archers.item.ArcherWeapons;
import net.archers.item.ArcherArmors;
import net.archers.item.misc.Misc;
import net.archers.content.ArcherSounds;
import net.archers.village.ArcherVillagers;
import net.archers.village.VillageStructures;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.Platform;
import net.spell_engine.PlatformEvents;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.api.util.TriState;
import net.tiny_config.ConfigManager;

public class ArchersMod {
    public static final String ID = "archers";

    public static ConfigManager<ArchersItemConfig> itemConfig = new ConfigManager<ArchersItemConfig>
            ("equipment", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            // Discards (and regenerates) files written before the RangedWeaponAPI 2.x `RangedConfig`
            // field rename — see ArchersItemConfig.
            .schemaVersion(ArchersItemConfig.SCHEMA_VERSION)
            .sanitize(true)
            .build();
    public static ConfigManager<ConfigFile.Effects> effectsConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    // `config/archers/villages.json` lives in `net.archers.fabric.village.FabricVillageStructures`:
    // StructurePoolAPI is Fabric-only on 1.20.1 (see net.archers.village.VillageStructures).

    public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
            ("tweaks", new TweaksConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static void init() {
        tweaksConfig.refresh();
        itemConfig.refresh();
        effectsConfig.refresh();
        if (Platform.util().isDevelopmentEnvironment()) {
            // Make sure items are enabled for datagen
            tweaksConfig.value.ignore_items_required_mods = true;
        }
        // Vanilla-village archery ranges. StructurePoolAPI is Fabric-only on 1.20.1 — the injector is
        // installed by the Fabric entrypoint and stays absent on Forge.
        VillageStructures.injectIfAvailable();

        // Apply some of the tweaks. Enchant-allow is routed through SpellEngine's loader-neutral
        // PlatformEvents.onAllowEnchanting (Fabric: EnchantmentEvents; NeoForge: IItemExtension mixin),
        // using SpellEngine's TriState — no Fabric API EnchantmentEvents in common.
        if (tweaksConfig.value.enable_infinity_for_crossbows) {
            PlatformEvents.onAllowEnchanting((enchantment, target) -> {
                // 1.20.1: `Enchantments.INFINITY` is the Enchantment itself, not a RegistryKey.
                if (target.getItem() instanceof CrossbowItem &&
                        enchantment.value() == Enchantments.INFINITY) {
                    return TriState.ALLOW;
                }
                return TriState.PASS;
            });
        }
    }

    public static void registerEntities() {
        // Each entity's base attributes are registered alongside its type build inside register(),
        // sourced from SpellEngine's central summoned-entity config.
        ArcherEntities.register();
    }

    public static void registerSounds() {
        ArcherSounds.register();
    }

    public static void registerBlocks() {
        ArcherBlocks.registerBlocks();
    }

    public static void registerItems() {
        Group.ARCHERS = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                .icon(() -> new ItemStack(ArcherArmors.archerArmorSet_T2.head))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
        ArcherBlocks.registerItems();

        // Blocks into the Archers creative tab. Dispatched by SpellEngine on both loaders
        // (Fabric `ItemGroupEvents` / Forge `BuildCreativeModeTabContentsEvent`).
        //
        // ORDER MATTERS: on both loaders the group modifiers run in *registration* order, so this listener
        // is installed BEFORE the weapon/armor registrations install SpellEngine's own listeners — that is
        // what puts the blocks at the front of the tab. It has to live here rather than in the loader
        // entrypoints: on Forge the tab event is posted per mod container in mod-load order, so anything an
        // Archers-owned listener adds would always land *after* SpellEngine's contributions.
        PlatformEvents.onItemGroupModify(Group.KEY, (content, context) -> {
            for (var entry : ArcherBlocks.all) {
                content.add(entry.item());
            }
        });

        Misc.register();
        ArcherWeapons.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
        ArcherArmors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    public static void registerEffects() {
        ArcherEffects.register(effectsConfig.value);
        effectsConfig.save();
    }

    public static void registerVillagers() {
        ArcherVillagers.registerVillagers();
    }
}