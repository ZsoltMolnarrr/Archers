package net.archers.forge;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.content.ArcherSounds;
import net.archers.effect.ArcherEffects;
import net.archers.entity.ArcherEntities;
import net.archers.forge.client.ForgeClientMod;
import net.archers.forge.compat.curios.QuiverCurios;
import net.archers.item.ArcherArmors;
import net.archers.item.ArcherWeapons;
import net.archers.item.Group;
import net.archers.item.Quivers;
import net.archers.item.misc.Misc;
import net.archers.village.ArcherVillagers;
import net.minecraft.registry.RegistryKeys;
import net.spell_engine.api.effect.Effects;
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
/// Forge locks every vanilla registry outside its own `RegisterEvent` window, and on 47.0-47.3 keeps the
/// *vanilla* wrapper locked even inside it, so registration goes through the `RegisterHelper` the event
/// hands out — one `event.register` block per registry, in `#register` below.
///
/// The Spirit Wolf's summon attributes need no listener here: `ArcherEntities.entityTypesToRegister()` hands them to
/// SpellEngine's `Platform.util().registerSummonedEntityAttributes`, which buffers them until SpellEngine's
/// own `EntityAttributeCreationEvent` listener flushes them — that event fires after the `ENTITY_TYPE`
/// window below, so the ordering holds.
@Mod(ArchersMod.ID)
public final class ForgeMod {
    /// Curios is optional (see `mods.toml`); every Curios-touching class must sit behind this check.
    public static final String CURIOS_ID = "curios";

    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        // Curios quiver integration — install the item factory BEFORE anything registers items
        // (`Quivers.register()` runs inside the ITEM window below). Gated on both mods: the factory
        // builds a `Quivers.QuiverItem` subclass (BundleAPI's CustomBundleItem) that implements
        // Curios' `ICurioItem`, so either mod being absent would be a NoClassDefFoundError.
        // Without it the quivers stay plain items — equippable, but silent.
        if (ModList.get().isLoaded("bundleapi") && ModList.get().isLoaded(CURIOS_ID)) {
            QuiverCurios.installFactory();
        }

        // Run our common setup (configs only — registers nothing). It also queues the vanilla-village
        // archery-range injection, which StructurePoolAPI applies on ServerAboutToStartEvent.
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

    /// Registration is duplicated here rather than delegated to `common`'s registerX() methods, because a
    /// plain `Registry.register` is not usable on this loader: Forge only clears the vanilla registry's own
    /// lock from 47.4.0 onwards, so on 47.0-47.3 and NeoForge 1.20.1 it throws "Can not register to a locked
    /// registry" even inside the correct `RegisterEvent` window, and our `mods.toml` declares
    /// `loaderVersion = "[47,)"`. The helper this event hands out is the API every build of [47,) sanctions,
    /// so Forge iterates the same content `common` exposes and registers it itself. `common` keeps its own
    /// vanilla-shaped registration for Fabric.
    ///
    /// `event.register` is a no-op unless its key matches the event's registry, so all seven blocks are
    /// declared unconditionally; Forge posts one event per registry and each block runs in exactly its own.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                // `ArcherSounds.Entry#entry` is deliberately not reproduced: the helper returns void and
                // nothing reads that field at runtime (see ArcherSounds#soundsToRegister).
                ArcherSounds.soundsToRegister().forEach(helper::register));

        event.register(RegistryKeys.BLOCK, helper -> {
            for (var entry : ArcherBlocks.all) {
                helper.register(entry.id(), entry.block());
            }
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            ArcherEffects.effectsToRegister(ArchersMod.effectsConfig.value).forEach(helper::register);
            // The helper returns void, so the `Effects.Entry#entry` registry entries gameplay reads are
            // filled back in from the registry afterwards.
            Effects.linkEntries(ArcherEffects.entries);
            ArchersMod.effectsConfig.save();
        });

        event.register(RegistryKeys.ENTITY_TYPE, helper ->
                ArcherEntities.entityTypesToRegister().forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            // Same order as `ArchersMod.registerItems()`, and the order is load-bearing: both the blocks
            // listener below and the item-group listeners SpellEngine's `itemsToRegister` helpers install
            // as they build are appended to one per-group list that replays in installation order, so the
            // blocks have to be queued before the weapons and armour are built.
            for (var entry : ArcherBlocks.all) {
                helper.register(entry.id(), entry.item());
            }
            ArchersMod.installBlockItemGroupListener();
            Misc.itemsToRegister().forEach(helper::register);
            ArcherWeapons.itemsToRegister(ArchersMod.itemConfig.value.ranged_weapons,
                    ArchersMod.itemConfig.value.melee_weapons).forEach(helper::register);
            ArcherArmors.itemsToRegister(ArchersMod.itemConfig.value.armor_sets).forEach(helper::register);
            ArchersMod.itemConfig.save();
        });

        // The item group gets its own block: `creative_mode_tab` is second to last of the ~66 registry
        // events while `item` is the 7th, so registering it from the ITEM window above would be a silent
        // key mismatch and the tab would simply never exist. `creative_mode_tab` is a vanilla-only registry,
        // so the helper falls through to a plain `Registry.register` — fine, only Forge-wrapped registries
        // are locked. Moving it out of the ITEM window does not disturb the tab ORDER: SpellEngine's
        // `onItemGroupModify` only appends to a list keyed by `Group.KEY`, which it replays at
        // `BuildCreativeModeTabContentsEvent`, long after both windows.
        event.register(RegistryKeys.ITEM_GROUP, helper ->
                helper.register(Group.ID, ArchersMod.createItemGroup()));

        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, helper -> {
            // Forge's POI registry callback (PointOfInterestTypeCallbacks) fills the block-state -> POI map
            // from the type's own block states as the entry is added, so nothing else is needed here.
            helper.register(ArcherVillagers.POI_ID,
                    new PointOfInterestType(ArcherVillagers.poiBlockStates(),
                            ArcherVillagers.POI_TICKET_COUNT, ArcherVillagers.POI_SEARCH_DISTANCE));
        });

        event.register(RegistryKeys.VILLAGER_PROFESSION, helper -> {
            helper.register(ArcherVillagers.PROFESSION_ID, ArcherVillagers.professionToRegister());
            // The helper returns void, so the field `VillagerTradesEvent` filters on is filled in afterwards.
            ArcherVillagers.linkProfessionEntry();
            ArcherVillagers.buildTrades();
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        // Forge 47's `accept` takes a Supplier<? extends ItemConvertible>. The blocks are NOT added here:
        // Forge posts this event per mod container in mod-load order, so anything an Archers-owned listener
        // adds always lands after SpellEngine's contributions. They go in through SpellEngine's
        // PlatformEvents.onItemGroupModify from ArchersMod.registerItems() instead, registered ahead of the
        // weapon/armor listeners so they come first.
        for (var entry : Misc.ENTRIES) {
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
