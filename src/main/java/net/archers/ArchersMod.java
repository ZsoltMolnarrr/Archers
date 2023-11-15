package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.config.TweaksConfig;
import net.archers.effect.Effects;
import net.archers.item.ArcherItems;
import net.archers.item.Group;
import net.archers.item.Weapons;
import net.archers.item.Armors;
import net.archers.item.misc.Misc;
import net.archers.util.SoundHelper;
import net.archers.village.ArcherVillagers;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.loot.LootConfig;
import net.spell_engine.api.loot.LootHelper;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_power.api.enchantment.EnchantmentRestriction;
import net.tinyconfig.ConfigManager;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    public static ConfigManager<ArchersItemConfig> itemConfig = new ConfigManager<ArchersItemConfig>
            ("items", Default.itemConfig)
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

    public static ConfigManager<LootConfig> lootConfig = new ConfigManager<>
            ("loot_v2", Default.lootConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();

    @Override
    public void onInitialize() {
        tweaksConfig.refresh();
        registerItemGroup();
        registerItems();
        SoundHelper.registerSounds();
        Effects.register();
        registerVillages();
        subscribeEvents();

        if (tweaksConfig.value.enable_infinity_for_crossbows) {
            EnchantmentRestriction.permit(Enchantments.INFINITY, itemStack -> itemStack.getItem() instanceof CrossbowItem);
        }
    }

    private void registerItemGroup() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.archerArmorSet_T2.head.asItem()))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
    }

    private void registerItems() {
        itemConfig.refresh();
        SpellBooks.createAndRegister(new Identifier(ID, "archer"), SpellContainer.ContentType.ARCHERY, Group.KEY);
        ArcherBlocks.register();
        Misc.register();
        Weapons.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    private void registerVillages() {
        villagesConfig.refresh();
        ArcherVillagers.register();
    }

    private void subscribeEvents() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootHelper.configure(id, tableBuilder, ArchersMod.lootConfig.value, ArcherItems.entries);
        });
    }
}