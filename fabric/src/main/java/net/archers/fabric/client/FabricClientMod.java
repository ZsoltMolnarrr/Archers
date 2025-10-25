package net.archers.fabric.client;

import net.archers.block.ArcherBlocks;
import net.archers.client.ArchersClientMod;
import net.archers.client.compat.AccessoriesRenderCompat;
import net.archers.fabric.client.trinkets.TrinketsRenderCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

import java.util.Objects;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArchersClientMod.init();

        // Fabric-specific render layer registration
        BlockRenderLayerMap.INSTANCE.putBlock(ArcherBlocks.WORKBENCH.block(), RenderLayer.getCutout());

        var slotCompat = FabricCompatFeatures.initSlotCompat();
        if (Objects.equals(slotCompat, "trinkets")) {
            TrinketsRenderCompat.init();
        } else if (Objects.equals(slotCompat,"accessories")) {
            AccessoriesRenderCompat.init();
        }
    }
}
