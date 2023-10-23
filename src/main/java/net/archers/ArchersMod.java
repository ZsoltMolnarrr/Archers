package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.config.EnchantmentsConfig;
import net.archers.config.WorldGenConfig;
import net.archers.item.Group;
import net.archers.item.Weapons;
import net.archers.item.armor.Armors;
import net.archers.item.misc.Misc;
import net.archers.util.SoundHelper;
import net.archers.village.ArcherVillagers;
import net.archers.village.VillageGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.spell.SpellContainer;
import net.tinyconfig.ConfigManager;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    public static ConfigManager<ArchersItemConfig> itemConfig = new ConfigManager<ArchersItemConfig>
            ("items", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<WorldGenConfig> worldGenConfig = new ConfigManager<>
            ("world_gen", Default.worldGen)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<EnchantmentsConfig> tweaksConfig = new ConfigManager<>
            ("tweaks", new EnchantmentsConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        registerItemGroup();
        registerItems();
        SoundHelper.registerSounds();
        registerVillages();
        subscribeEvents();
        tweaksConfig.refresh();
    }

    private void registerItemGroup() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.archerArmorSet_T1.head.asItem()))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
    }

    private void registerItems() {
        itemConfig.refresh();
        SpellBooks.createAndRegister(new Identifier(ID, "archer"), SpellContainer.ContentType.ARCHERY, Group.KEY);
        ArcherBlocks.register();
        Misc.register();
        Weapons.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    private void registerVillages() {
        worldGenConfig.refresh();
        ArcherVillagers.register();
        ServerLifecycleEvents.SERVER_STARTING.register(VillageGeneration::init);
    }

    private void subscribeEvents() {
    }
}