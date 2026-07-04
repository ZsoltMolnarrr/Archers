package net.archers.neoforge.client;

import net.archers.ArchersMod;
import net.archers.client.ArchersClientMod;
import net.archers.client.compat.AccessoriesRenderCompat;
import net.archers.client.entity.DirewolfEntityModel;
import net.archers.client.entity.DirewolfEntityRenderer;
import net.archers.entity.SpiritWolfEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = ArchersMod.ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ArchersClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));

        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            AccessoriesRenderCompat.init();
        }
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DirewolfEntityModel.TEXTURE, DirewolfEntityModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SpiritWolfEntity.TYPE, DirewolfEntityRenderer::new);
    }
}