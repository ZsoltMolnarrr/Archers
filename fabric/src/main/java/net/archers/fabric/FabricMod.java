package net.archers.fabric;

import net.archers.ArchersMod;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.item.misc.Misc;
import net.archers.fabric.village.FabricVillageStructures;
import net.archers.village.ArcherVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // StructurePoolAPI is Fabric-only on 1.20.1 — install the village injector before the common
        // setup runs (ArchersMod.init() calls VillageStructures.injectIfAvailable()).
        FabricVillageStructures.install();
        // Run our common setup.
        ArchersMod.init();
        ArchersMod.registerEntities();
        ArchersMod.registerSounds();
        ArchersMod.registerBlocks();
        ArchersMod.registerItems();
        ArchersMod.registerEffects();

        // Villager POI + trades — Fabric API registration (loader-specific; NeoForge does its own).
        PointOfInterestHelper.register(ArcherVillagers.POI_ID,
                ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE,
                ArcherVillagers.poiBlockStates());
        ArchersMod.registerVillagers(); // registers the profession + builds ArcherVillagers.TRADES
        ArcherVillagers.TRADES.forEach((tier, factories) ->
                TradeOfferHelper.registerVillagerOffers(ArcherVillagers.PROFESSION, tier,
                        list -> list.addAll(factories)));

        // Creative-tab placement (Archers group) — Fabric API. The blocks are NOT added here: they go in
        // through SpellEngine's loader-neutral PlatformEvents.onItemGroupModify from
        // ArchersMod.registerItems(), registered ahead of the weapon/armor listeners so they come first.
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for (var entry : Misc.ENTRIES) {
                content.add(entry.item());
            }
            // Gate BEFORE touching Quivers: reading the static field forces the JVM to link/verify
            // Quivers, whose factory references BundleAPI's CustomBundleItem — a NoClassDefFoundError
            // when BundleAPI is absent. The empty entries list never even gets iterated otherwise.
            if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
                for (var entry : Quivers.entries) {
                    content.add(entry.item());
                }
            }
        });
    }
}
