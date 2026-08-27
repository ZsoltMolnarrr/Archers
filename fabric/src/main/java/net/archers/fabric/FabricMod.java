package net.archers.fabric;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.item.misc.Misc;
import net.archers.village.ArcherVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        ArchersMod.init();
        ArchersMod.registerEntities();
        ArchersMod.registerSounds();
        ArchersMod.registerBlocks();
        ArchersMod.registerItems();
        ArchersMod.registerEffects();

        // Villager POI — Fabric API registration (loader-specific; NeoForge does its own).
        PoiHelper.register(ArcherVillagers.POI_ID,
                ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE,
                ArcherVillagers.poiBlockStates());
        // Offers themselves are data-driven since 26.1 (data/archers/{villager_trade,trade_set}); the
        // profession only carries the trade-set keys, so there is nothing loader-specific left to register.
        ArchersMod.registerVillagers();

        // Creative-tab placement (Archers group) — Fabric API.
        CreativeModeTabEvents.modifyOutputEvent(Group.KEY).register(content -> {
            for (var entry : Misc.ENTRIES) {
                content.accept(entry.item());
            }
            for (var entry : ArcherBlocks.all) {
                content.accept(entry.item());
            }
            // Gate BEFORE touching Quivers: reading the static field forces the JVM to link/verify
            // Quivers, whose factory references BundleAPI's CustomBundleItem — a NoClassDefFoundError
            // when BundleAPI is absent. The empty entries list never even gets iterated otherwise.
            if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
                for (var entry : Quivers.entries) {
                    content.accept(entry.item());
                }
            }
        });
    }
}
