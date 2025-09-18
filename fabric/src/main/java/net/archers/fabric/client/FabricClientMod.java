package net.archers.fabric.client;

import net.archers.block.ArcherBlocks;
import net.archers.client.ArchersClientMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArchersClientMod.init();

        // Fabric-specific render layer registration
        BlockRenderLayerMap.INSTANCE.putBlock(ArcherBlocks.WORKBENCH.block(), RenderLayer.getCutout());
    }
}
