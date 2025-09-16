package net.archers.fabric.client;

import net.archers.client.ArchersClientMod;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArchersClientMod.init();
    }
}
