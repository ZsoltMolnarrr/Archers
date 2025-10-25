package net.archers.fabric.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.archers.block.ArcherBlocks;
import net.archers.client.ArchersClientMod;
import net.archers.fabric.client.trinkets.QuiverRenderer;
import net.archers.item.Quivers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArchersClientMod.init();

        // Fabric-specific render layer registration
        BlockRenderLayerMap.INSTANCE.putBlock(ArcherBlocks.WORKBENCH.block(), RenderLayer.getCutout());

        for (var entry: Quivers.entries) {
            String modelName = entry.id().getPath();
            TrinketRendererRegistry.registerRenderer(entry.item(), new QuiverRenderer("quiver/" + modelName));
        }
    }
}
