package net.archers.fabric;

import net.archers.ArchersMod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        ArchersMod.init();
        ArchersMod.registerSounds();
        ArchersMod.registerBlocks();
        ArchersMod.registerItems();
        ArchersMod.registerEffects();
        ArchersMod.registerPOI();
        ArchersMod.registerVillagers();
    }
}
