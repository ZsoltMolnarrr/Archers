package net.archers.neoforge;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.neoforge.compat.curios.QuiverCurios;
import net.archers.item.misc.Misc;
import net.archers.village.ArcherVillagers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ArchersMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        ArchersMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Creative-tab placement (Archers group) — NeoForge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
        // Villager trades — game-bus event (fired per profession); replaces Fabric API's TradeOfferHelper.
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, NeoForgeMod::onVillagerTrades);
        // Quiver equip sound, via a Curios capability on the (third-party) bundle items.
        if (ModList.get().isLoaded("curios")) {
            modBus.addListener(RegisterCapabilitiesEvent.class, QuiverCurios::registerCapabilities);
        }
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            ArchersMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            ArchersMod.registerEntities();
            ArchersMod.registerItems();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            ArchersMod.registerBlocks();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            ArchersMod.registerEffects();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // POI registration — vanilla registry insert. NeoForge's POI registry callback wires the
            // block-state -> POI mapping from the type's block states, so no Fabric API helper is needed.
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                Registry.register(Registries.POINT_OF_INTEREST_TYPE, ArcherVillagers.POI_ID,
                        new PointOfInterestType(ArcherVillagers.poiBlockStates(),
                                ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE));
            } catch (Exception e) { }
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            ArchersMod.registerVillagers(); // registers the profession + builds ArcherVillagers.TRADES
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        for (var entry : Misc.ENTRIES) {
            event.add(entry.item());
        }
        for (var entry : ArcherBlocks.all) {
            event.add(entry.item());
        }
        // Gate BEFORE touching Quivers (BundleAPI class-verification guard, as in FabricMod).
        if (ModList.get().isLoaded("bundleapi")) {
            for (var entry : Quivers.entries) {
                event.add(entry.item());
            }
        }
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != ArcherVillagers.PROFESSION_KEY) {
            return;
        }
        ArcherVillagers.TRADES.forEach((tier, factories) -> {
            var tierList = event.getTrades().get(tier.intValue());
            if (tierList != null) {
                tierList.addAll(factories);
            }
        });
    }
}
