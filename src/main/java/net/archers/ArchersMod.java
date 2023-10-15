package net.archers;

import net.archers.item.Group;
import net.archers.util.SoundHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    @Override
    public void onInitialize() {
        preInit();
        SoundHelper.registerSounds();
        subscribeEvents();
    }

    private void preInit() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.ARROW))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
    }

    private void subscribeEvents() {
    }
}