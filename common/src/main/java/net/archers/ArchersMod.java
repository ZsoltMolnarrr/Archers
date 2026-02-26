package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.config.TweaksConfig;
import net.archers.effect.ArcherEffects;
import net.archers.item.Group;
import net.archers.item.ArcherWeapons;
import net.archers.item.ArcherArmors;
import net.archers.item.misc.Misc;
import net.archers.content.ArcherSounds;
import net.archers.village.ArcherVillagers;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.ConfigFile;
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
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            // Make sure items are enabled for datagen
            tweaksConfig.value.ignore_items_required_mods = true;
        }
        if (!FabricLoader.getInstance().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(ArchersMod.villagesConfig.value);
        }

        // Apply some of the tweaks
        if (tweaksConfig.value.enable_infinity_for_crossbows) {
            EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> {
                if (target.getItem() instanceof CrossbowItem &&
                        enchantment.getKey().get().getValue().equals(Enchantments.INFINITY.getValue())) {
                    return TriState.TRUE;
                }
                return TriState.DEFAULT;
            });
        }
    }

    public static void registerSounds() {
        ArcherSounds.register();
    }

    public static void registerBlocks() {
        ArcherBlocks.register();
    }

    public static void registerItems() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ArcherArmors.archerArmorSet_T2.head))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
        Misc.register();
        ArcherWeapons.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
        ArcherArmors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    public static void registerEffects() {
        ArcherEffects.register(effectsConfig.value);
        effectsConfig.save();
    }

    public static void registerPOI() {
        ArcherVillagers.registerPOI();
    }

    public static void registerVillagers() {
        ArcherVillagers.registerVillagers();
    }
}