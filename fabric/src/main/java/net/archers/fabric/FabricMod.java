package net.archers.fabric;

import net.archers.ArchersMod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ArchersMod.init();
    }
}
