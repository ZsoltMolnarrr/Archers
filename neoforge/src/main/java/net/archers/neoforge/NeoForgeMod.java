package net.archers.neoforge;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.neoforge.compat.curios.QuiverCurios;
import net.archers.item.misc.Misc;
import net.archers.village.ArcherVillagers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ArchersMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        ArchersMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Creative-tab placement (Archers group) — NeoForge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
        // Quiver equip sound, via a Curios capability on the (third-party) bundle items.
        if (ModList.get().isLoaded("curios")) {
            modBus.addListener(RegisterCapabilitiesEvent.class, QuiverCurios::registerCapabilities);
        }
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, reg -> {
            ArchersMod.registerSounds();
        });
        event.register(Registries.ITEM, reg -> {
            ArchersMod.registerEntities();
            ArchersMod.registerItems();
        });
        event.register(Registries.BLOCK, reg -> {
            ArchersMod.registerBlocks();
        });
        event.register(Registries.MOB_EFFECT, reg -> {
            ArchersMod.registerEffects();
        });
        event.register(Registries.POINT_OF_INTEREST_TYPE, reg -> {
            // POI registration — vanilla registry insert. NeoForge's POI registry callback wires the
            // block-state -> POI mapping from the type's block states, so no Fabric API helper is needed.
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, ArcherVillagers.POI_ID,
                        new PoiType(ArcherVillagers.poiBlockStates(),
                                ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE));
            } catch (Exception e) { }
        });
        event.register(Registries.VILLAGER_PROFESSION, reg -> {
            // Offers themselves are data-driven since 26.1 (data/archers/{villager_trade,trade_set});
            // the profession only carries the trade-set keys.
            ArchersMod.registerVillagers();
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        for (var entry : Misc.ENTRIES) {
            event.accept(entry.item());
        }
        for (var entry : ArcherBlocks.all) {
            event.accept(entry.item());
        }
        // Gate BEFORE touching Quivers (BundleAPI class-verification guard, as in FabricMod).
        if (ModList.get().isLoaded("bundleapi")) {
            for (var entry : Quivers.entries) {
                event.accept(entry.item());
            }
        }
    }
}
