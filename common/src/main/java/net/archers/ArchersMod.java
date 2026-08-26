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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.rpg_foundation.structure_pool.api.StructurePoolAPI;
import net.rpg_foundation.structure_pool.api.StructurePoolConfig;
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
            .sanitize(true)
            .build();
    public static ConfigManager<ConfigFile.Effects> effectsConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StructurePoolConfig> villagesConfig = new ConfigManager<>
            ("villages", Default.villages)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

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
        villagesConfig.refresh();
        if (Platform.util().isDevelopmentEnvironment()) {
            // Make sure items are enabled for datagen
            tweaksConfig.value.ignore_items_required_mods = true;
        }
        if (!Platform.util().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(ArchersMod.villagesConfig.value);
        }

        // Apply some of the tweaks. Enchant-allow is routed through SpellEngine's loader-neutral
        // PlatformEvents.onAllowEnchanting (Fabric: EnchantmentEvents; NeoForge: IItemExtension mixin),
        // using SpellEngine's TriState — no Fabric API EnchantmentEvents in common.
        if (tweaksConfig.value.enable_infinity_for_crossbows) {
            PlatformEvents.onAllowEnchanting((enchantment, target) -> {
                if (target.getItem() instanceof CrossbowItem &&
                        enchantment.unwrapKey().get().identifier().equals(Enchantments.INFINITY.identifier())) {
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
        ArcherBlocks.register();
    }

    public static void registerItems() {
        Group.ARCHERS = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
                .icon(() -> new ItemStack(ArcherArmors.archerArmorSet_T2.head))
                .title(Component.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Group.KEY, Group.ARCHERS);
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