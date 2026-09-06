package net.archers.village;

import com.google.common.collect.ImmutableSet;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.ArcherWeapons;
import net.archers.item.ArcherArmors;
import net.archers.content.ArcherSounds;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ArcherVillagers {
    public static final String ARCHERY_ARTISAN = "archery_artisan";
    public static final Identifier POI_ID = new Identifier(ArchersMod.ID, ARCHERY_ARTISAN);
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;

    /// The archer-workbench workstation block states for the POI. Registration itself is loader-specific
    /// (Fabric: `PointOfInterestHelper`; NeoForge: a plain `Registry.register` of a `PointOfInterestType`,
    /// whose block-state mapping NeoForge wires via its POI registry callback) — done in each platform's
    /// entrypoint; this only exposes the shared state set.
    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(ArcherBlocks.WORKBENCH.block().getStateManager().getStates());
    }

    /// The registered archery-artisan profession, set by {@link #registerVillagers()}. Read by the
    /// loader-specific trade-offer registration (Fabric `TradeOfferHelper` / NeoForge `VillagerTradesEvent`).
    public static VillagerProfession PROFESSION;

    /// Trade offers per merchant tier (1..5), populated by {@link #registerVillagers()}. Actual registration
    /// with the game is loader-specific and lives in each platform's entrypoint.
    public static final LinkedHashMap<Integer, List<TradeOffers.Factory>> TRADES = new LinkedHashMap<>();

    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = new Identifier(ArchersMod.ID, name);
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(ArchersMod.ID, name), new VillagerProfession(
                id.toString(),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                ArcherSounds.WORKBENCH.soundEvent())
        );
    }

    public static void registerVillagers() {
        PROFESSION = registerProfession(
                ARCHERY_ARTISAN,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), POI_ID));

        TRADES.clear();
        TRADES.put(1, List.of(
                // 1.20.1's SellItemFactory has no 6-arg `Item` overload — only the ItemStack one.
                new TradeOffers.SellItemFactory(new ItemStack(Items.ARROW), 2, 8, 128, 3, 0.01f),
                buyForEmeralds(Items.LEATHER, 8, 12, 6, 5)
        ));
        TRADES.put(2, List.of(
                new TradeOffers.SellItemFactory(ArcherWeapons.composite_longbow.item(), 6, 1, 16),
                new TradeOffers.SellItemFactory(ArcherArmors.archerArmorSet_T1.head, 15, 1, 18),
                buyForEmeralds(Items.STRING, 6, 12, 8, 3)
        ));
        TRADES.put(3, List.of(
                new TradeOffers.SellItemFactory(ArcherArmors.archerArmorSet_T1.feet, 15, 1, 18),
                buyForEmeralds(Items.REDSTONE, 12, 12, 5, 8),
                new TradeOffers.SellItemFactory(ArcherArmors.archerArmorSet_T1.legs, 15, 1, 18)
        ));
        TRADES.put(4, List.of(
                new TradeOffers.SellItemFactory(ArcherArmors.archerArmorSet_T1.chest, 15, 1, 18),
                new TradeOffers.SellItemFactory(Items.SCUTE, 20, 12, 10)
        ));
        TRADES.put(5, List.of(
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        ArcherWeapons.royal_longbow.item(), 40, 3, 30, 0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        ArcherWeapons.mechanic_shortbow.item(), 40, 3, 30, 0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        ArcherWeapons.rapid_crossbow.item(), 40, 3, 30, 0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        ArcherWeapons.heavy_crossbow.item(), 40, 3, 30, 0F).create(entity, random)
        ));
    }

    /// 1.20.1 has no `TradeOffers.BuyItemFactory` (only `BuyForOneEmeraldFactory`, which is fixed at one
    /// emerald), so the "villager buys N of an item for M emeralds" offer is rebuilt on the raw
    /// `TradeOffer` constructor with the same 0.05 price multiplier 1.21's factory uses.
    private static TradeOffers.Factory buyForEmeralds(Item item, int count, int maxUses, int experience, int emeralds) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), new ItemStack(Items.EMERALD, emeralds), maxUses, experience, 0.05F);
    }
}
