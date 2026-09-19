package net.archers.neoforge.client;

import net.archers.ArchersMod;
import net.archers.client.ArchersClientMod;
import net.archers.client.entity.DirewolfEntityModel;
import net.archers.client.entity.DirewolfEntityRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.entity.ArcherEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = ArchersMod.ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    /// TODO 26.3 (Curios): the Curios quiver renderer (`client/curios/**`) is excluded from the compile while
    /// `enable_curios=false` (see gradle.properties), so it is reached reflectively instead of by a direct
    /// reference. With the gate restored the class is present again and this resolves it as before.
    private static final String CURIOS_RENDER_COMPAT = "net.archers.neoforge.client.curios.CuriosRenderCompat";

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ArchersClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));

        if (ModList.get().isLoaded("curios")) {
            try {
                Class.forName(CURIOS_RENDER_COMPAT).getMethod("init").invoke(null);
            } catch (ClassNotFoundException e) {
                // built without the Curios integration
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to initialize Curios render compat", e);
            }
        }

        // Archers' custom tooltip lines — NeoForge game-bus event (replaces Fabric API's ItemTooltipCallback).
        NeoForge.EVENT_BUS.addListener(ItemTooltipEvent.class, tooltip ->
                ArchersTooltip.addLines(tooltip.getItemStack(), tooltip.getToolTip()));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DirewolfEntityModel.TEXTURE, DirewolfEntityModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ArcherEntities.SPIRIT_WOLF.type, DirewolfEntityRenderer::new);
    }
}