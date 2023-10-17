package net.archers;

import net.archers.item.Group;
import net.archers.item.Misc;
import net.archers.util.SoundHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    @Override
    public void onInitialize() {
        registerItemGroup();
        registerItems();
        SoundHelper.registerSounds();
        subscribeEvents();
    }

    private void registerItemGroup() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.ARROW))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
    }

    private void registerItems() {
        Misc.register();
    }

    private void subscribeEvents() {
    }
}