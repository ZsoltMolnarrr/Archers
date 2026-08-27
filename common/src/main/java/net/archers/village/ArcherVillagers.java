package net.archers.village;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.content.ArcherSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ArcherVillagers {
    public static final String ARCHERY_ARTISAN = "archery_artisan";
    public static final Identifier POI_ID = Identifier.fromNamespaceAndPath(ArchersMod.ID, ARCHERY_ARTISAN);
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;

    /// The archer-workbench workstation block states for the POI. Registration itself is loader-specific
    /// (Fabric: `PointOfInterestHelper`; NeoForge: a plain `Registry.register` of a `PointOfInterestType`,
    /// whose block-state mapping NeoForge wires via its POI registry callback) — done in each platform's
    /// entrypoint; this only exposes the shared state set.
    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(ArcherBlocks.WORKBENCH.block().getStateDefinition().getPossibleStates());
    }

    /// The registered archery-artisan profession, set by {@link #registerVillagers()}.
    public static VillagerProfession PROFESSION;

    /// Registry key of {@link #PROFESSION}.
    public static final ResourceKey<VillagerProfession> PROFESSION_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(ArchersMod.ID, ARCHERY_ARTISAN));

    /// Trade sets per merchant tier (1..5). Since 26.1 the offers themselves are data-driven:
    /// `data/archers/villager_trade/archery_artisan/<level>/<trade>.json` (one offer each), grouped by
    /// `data/archers/tags/villager_trade/archery_artisan/level_<n>.json` and drawn from by
    /// `data/archers/trade_set/archery_artisan/level_<n>.json` (`amount: 2`, mirroring vanilla's two
    /// random offers per tier). The profession only carries the trade-set keys.
    public static ResourceKey<TradeSet> tradeSet(int level) {
        return ResourceKey.create(Registries.TRADE_SET,
                Identifier.fromNamespaceAndPath(ArchersMod.ID, ARCHERY_ARTISAN + "/level_" + level));
    }

    private static Int2ObjectMap<ResourceKey<TradeSet>> tradeSets() {
        return Int2ObjectMap.ofEntries(
                Int2ObjectMap.entry(1, tradeSet(1)),
                Int2ObjectMap.entry(2, tradeSet(2)),
                Int2ObjectMap.entry(3, tradeSet(3)),
                Int2ObjectMap.entry(4, tradeSet(4)),
                Int2ObjectMap.entry(5, tradeSet(5))
        );
    }

    public static VillagerProfession registerProfession(String name, ResourceKey<PoiType> workStation) {
        var id = Identifier.fromNamespaceAndPath(ArchersMod.ID, name);
        // `VillagerProfession.id` became a display `Text` in 1.21.11 (vanilla builds
        // `entity.<ns>.villager.<path>`). Keep the key the existing translations already use:
        // `entity.minecraft.villager.archers.archery_artisan`.
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, id, new VillagerProfession(
                Component.translatable("entity.minecraft.villager." + id.getNamespace() + "." + id.getPath()),
                (entry) -> entry.is(workStation),
                (entry) -> entry.is(workStation),
                ImmutableSet.of(),
                ImmutableSet.of(),
                ArcherSounds.WORKBENCH.soundEvent(),
                tradeSets())
        );
    }

    public static void registerVillagers() {
        PROFESSION = registerProfession(
                ARCHERY_ARTISAN,
                ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), POI_ID));
    }
}
