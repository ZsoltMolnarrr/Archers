package net.archers.fabric.client;

import net.archers.block.ArcherBlocks;
import net.archers.client.ArchersClientMod;
import net.archers.client.entity.DirewolfEntityModel;
import net.archers.client.entity.DirewolfEntityRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.entity.ArcherEntities;
import net.archers.fabric.client.trinkets.TrinketsRenderCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

import java.util.Objects;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArchersClientMod.init();

        // Entity model layers + renderers
        EntityModelLayerRegistry.registerModelLayer(DirewolfEntityModel.TEXTURE, DirewolfEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(ArcherEntities.SPIRIT_WOLF.type, DirewolfEntityRenderer::new);

        // Fabric-specific render layer registration
        BlockRenderLayerMap.putBlock(ArcherBlocks.WORKBENCH.block(), BlockRenderLayer.CUTOUT);

        // Archers' custom tooltip lines — Fabric API (loader-specific; NeoForge uses ItemTooltipEvent).
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) ->
                ArchersTooltip.addLines(stack, lines));

        var slotCompat = FabricCompatFeatures.initSlotCompat();
        if (Objects.equals(slotCompat, "trinkets")) {
            TrinketsRenderCompat.init();
        }
    }
}
