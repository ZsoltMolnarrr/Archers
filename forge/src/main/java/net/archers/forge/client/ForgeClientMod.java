package net.archers.forge.client;

import net.archers.client.ArchersClientMod;
import net.archers.client.entity.DirewolfEntityModel;
import net.archers.client.entity.DirewolfEntityRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.entity.ArcherEntities;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.spell_engine.client.gui.ConfigMenuScreen;

/// Client-only wiring for Forge 47; only touched from {@link net.archers.forge.ForgeMod} behind a
/// `Dist.CLIENT` check. Mod-bus listeners are registered explicitly (Forge 47's `@EventBusSubscriber`
/// scanning is avoided so the class is never loaded on a dedicated server).
///
/// 1.20.1 port of the NeoForge client entrypoint: `IConfigScreenFactory` becomes
/// `ConfigScreenHandler.ConfigScreenFactory`.
///
/// The workbench's cutout render layer needs no wiring here — its block model carries
/// `"render_type": "minecraft:cutout"`, the Forge model extension; only Fabric needs `BlockRenderLayerMap`.
public final class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterLayerDefinitions.class,
                ForgeClientMod::onRegisterLayerDefinitions);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class,
                ForgeClientMod::onRegisterRenderers);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        ArchersClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new ConfigMenuScreen(parent)));

        // Archers' custom tooltip lines — game-bus event (replaces Fabric API's ItemTooltipCallback).
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ItemTooltipEvent.class, tooltip ->
                ArchersTooltip.addLines(tooltip.getItemStack(), tooltip.getToolTip()));
    }

    private static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DirewolfEntityModel.TEXTURE, DirewolfEntityModel::getTexturedModelData);
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ArcherEntities.SPIRIT_WOLF.type, DirewolfEntityRenderer::new);
    }
}
