package net.archers.forge;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.forge.client.ForgeClientMod;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.item.misc.Misc;
import net.archers.village.ArcherVillagers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

/// Forge 47 entrypoint (1.20.1 port of the NeoForge entrypoint).
///
/// Forge locks every vanilla registry outside its own `RegisterEvent` window, so each `registerX()`
/// call sits inside the window of the registry it writes to — which is why block and block-item
/// registration are split (`ArcherBlocks.registerBlocks()` / `registerItems()`).
///
/// The Spirit Wolf's summon attributes need no listener here: `ArcherEntities.register()` hands them to
/// SpellEngine's `Platform.util().registerSummonedEntityAttributes`, which buffers them until SpellEngine's
/// own `EntityAttributeCreationEvent` listener flushes them — that event fires after the `ENTITY_TYPE`
/// window below, so the ordering holds.
@Mod(ArchersMod.ID)
public final class ForgeMod {
    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        // Run our common setup (configs only — registers nothing). The vanilla-village archery-range
        // injection inside it is a no-op on Forge: StructurePoolAPI is Fabric-only on 1.20.1, so no
        // VillageStructures.Injector is installed here.
        ArchersMod.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Explicit event classes: Forge 47's plain addListener(Consumer) infers the event type from the
        // lambda via TypeTools, which is fragile; the 4-arg overload takes it directly.
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Creative-tab placement (Archers group) — mod-bus event (replaces Fabric API's ItemGroupEvents).
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class,
                ForgeMod::buildTabContents);
        // Villager trades — game-bus event (fired per profession); replaces Fabric API's TradeOfferHelper.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, VillagerTradesEvent.class,
                ForgeMod::onVillagerTrades);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> ArchersMod.registerSounds());
        event.register(RegistryKeys.BLOCK, reg -> ArchersMod.registerBlocks());
        event.register(RegistryKeys.STATUS_EFFECT, reg -> ArchersMod.registerEffects());
        event.register(RegistryKeys.ENTITY_TYPE, reg -> ArchersMod.registerEntities());
        event.register(RegistryKeys.ITEM, reg -> {
            // Also registers the block items, the quivers and the `archers:generic` item group:
            // ITEM_GROUP is a vanilla-only registry (not Forge-wrapped), unfrozen for the whole
            // RegisterEvent phase.
            ArchersMod.registerItems();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // Plain vanilla registry insert. Forge's POI registry callback (PointOfInterestTypeCallbacks)
            // fills the block-state -> POI map from the type's own block states, so no helper is needed.
            Registry.register(Registries.POINT_OF_INTEREST_TYPE, ArcherVillagers.POI_ID,
                    new PointOfInterestType(ArcherVillagers.poiBlockStates(),
                            ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE));
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            ArchersMod.registerVillagers(); // registers the profession + builds ArcherVillagers.TRADES
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        // Forge 47's `accept` takes a Supplier<? extends ItemConvertible>.
        for (var entry : Misc.ENTRIES) {
            event.accept(() -> entry.item());
        }
        for (var entry : ArcherBlocks.all) {
            event.accept(() -> entry.item());
        }
        // Gate BEFORE touching Quivers: reading the static field forces the JVM to link/verify Quivers,
        // whose factory references BundleAPI's CustomBundleItem — a NoClassDefFoundError when BundleAPI
        // is absent. The empty entries list never even gets iterated otherwise.
        if (ModList.get().isLoaded("bundleapi")) {
            for (var entry : Quivers.entries) {
                event.accept(() -> entry.item());
            }
        }
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != ArcherVillagers.PROFESSION) {
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
