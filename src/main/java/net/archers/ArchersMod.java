package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.item.Group;
import net.archers.item.Weapons;
import net.archers.item.armor.Armors;
import net.archers.item.misc.Misc;
import net.archers.util.SoundHelper;
import net.archers.villager.ArcherVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.tinyconfig.ConfigManager;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    public static ConfigManager<ArchersItemConfig> itemConfig = new ConfigManager<ArchersItemConfig>
            ("items", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        registerItemGroup();
        registerItems();
        ArcherVillagers.register();
        SoundHelper.registerSounds();
        subscribeEvents();
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
        ArcherBlocks.register();
        Misc.register();
        Weapons.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    private void subscribeEvents() {
    }
}