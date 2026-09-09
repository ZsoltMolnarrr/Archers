package net.archers.village;

import com.google.common.collect.ImmutableSet;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.ArcherWeapons;
import net.archers.item.ArcherArmors;
import net.archers.content.ArcherSounds;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ArcherVillagers {
    public static final String ARCHERY_ARTISAN = "archery_artisan";
    public static final Identifier PROFESSION_ID = new Identifier(ArchersMod.ID, ARCHERY_ARTISAN);
    public static final Identifier POI_ID = new Identifier(ArchersMod.ID, ARCHERY_ARTISAN);
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;

    /// The archer-workbench workstation block states for the POI. Registration itself is loader-specific
    /// (Fabric: `PointOfInterestHelper`; Forge: a `PointOfInterestType` handed to the `RegisterEvent`
    /// helper, whose block-state mapping Forge wires via its POI registry callback) — done in each
    /// platform's entrypoint; this only exposes the shared state set.
    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(ArcherBlocks.WORKBENCH.block().getStateManager().getStates());
    }

    /// The registered archery-artisan profession, set by {@link #registerVillagers()} (Fabric) or by
    /// {@link #linkProfessionEntry()} (Forge). Read by the loader-specific trade-offer registration
    /// (Fabric `TradeOfferHelper` / Forge `VillagerTradesEvent`).
    public static VillagerProfession PROFESSION;

    /// Trade offers per merchant tier (1..5), populated by {@link #buildTrades()}. Actual registration
    /// with the game is loader-specific and lives in each platform's entrypoint.
    public static final LinkedHashMap<Integer, List<TradeOffers.Factory>> TRADES = new LinkedHashMap<>();

    public static VillagerProfession createProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = new Identifier(ArchersMod.ID, name);
        return new VillagerProfession(
                id.toString(),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                ArcherSounds.WORKBENCH.soundEvent()
        );
    }

    private static VillagerProfession archeryArtisanProfession;

    /// Builds the archery-artisan profession once, keyed by {@link #PROFESSION_ID}. Creation only —
    /// nothing is written to a registry here, so a loader that registers the profession itself (Forge)
    /// hands this to its own registration API instead of duplicating the construction.
    public static VillagerProfession professionToRegister() {
        if (archeryArtisanProfession == null) {
            archeryArtisanProfession = createProfession(ARCHERY_ARTISAN,
                    RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), POI_ID));
        }
        return archeryArtisanProfession;
    }

    /// Reads {@link #PROFESSION} back out of the registry, for a loader that registered the profession
    /// itself. `VillagerTradesEvent` filtering compares against this field, and Forge's `RegisterEvent`
    /// helper returns void, so Forge calls this straight after its registration. Throws if the profession
    /// is missing — which is also what catches a silently mis-keyed `event.register` block.
    public static void linkProfessionEntry() {
        if (PROFESSION == null) {
            PROFESSION = Registries.VILLAGER_PROFESSION
                    .getOrEmpty(PROFESSION_ID)
                    .orElseThrow(() -> new IllegalStateException(
                            "Villager profession " + PROFESSION_ID + " is not in the registry — register it first"));
        }
    }

    public static void registerVillagers() {
        PROFESSION = Registry.register(Registries.VILLAGER_PROFESSION, PROFESSION_ID, professionToRegister());
        buildTrades();
    }

    /// Populates {@link #TRADES}. Creation only — the offers are handed to the game by each platform's
    /// entrypoint (Fabric `TradeOfferHelper` / Forge `VillagerTradesEvent`).
    public static void buildTrades() {
        TRADES.clear();
        TRADES.put(1, List.of(
                sell(Items.ARROW, 2, 8, 128, 3, 0.01F),
                buyForEmeralds(Items.LEATHER, 8, 12, 6, 5)
        ));
        TRADES.put(2, List.of(
                sell(ArcherWeapons.composite_longbow.item(), 6, 1, 16),
                sell(ArcherArmors.archerArmorSet_T1.head, 15, 1, 18),
                buyForEmeralds(Items.STRING, 6, 12, 8, 3)
        ));
        TRADES.put(3, List.of(
                sell(ArcherArmors.archerArmorSet_T1.feet, 15, 1, 18),
                buyForEmeralds(Items.REDSTONE, 12, 12, 5, 8),
                sell(ArcherArmors.archerArmorSet_T1.legs, 15, 1, 18)
        ));
        TRADES.put(4, List.of(
                sell(ArcherArmors.archerArmorSet_T1.chest, 15, 1, 18),
                sell(Items.SCUTE, 20, 12, 10)
        ));
        TRADES.put(5, List.of(
                sellEnchanted(ArcherWeapons.royal_longbow.item(), 40, 3, 30, 0F),
                sellEnchanted(ArcherWeapons.mechanic_shortbow.item(), 40, 3, 30, 0F),
                sellEnchanted(ArcherWeapons.rapid_crossbow.item(), 40, 3, 30, 0F),
                sellEnchanted(ArcherWeapons.heavy_crossbow.item(), 40, 3, 30, 0F)
        ));
    }

    /// 1.20.1 has no `TradeOffers.BuyItemFactory` (only `BuyForOneEmeraldFactory`, which is fixed at one
    /// emerald), so the "villager buys N of an item for M emeralds" offer is rebuilt on the raw
    /// `TradeOffer` constructor with the same 0.05 price multiplier 1.21's factory uses.
    private static TradeOffers.Factory buyForEmeralds(Item item, int count, int maxUses, int experience, int emeralds) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), new ItemStack(Items.EMERALD, emeralds), maxUses, experience, 0.05F);
    }

    /// Mirrors `TradeOffers.SellItemFactory(Item, price, count, experience)` — the 4-argument overload,
    /// whose `maxUses` is vanilla's default of 12.
    ///
    /// `TradeOffers.SellItemFactory` is a **package-private** class in the real 1.20.1 jar and stays so
    /// after Forge's access transformer, so touching it from our own package throws
    /// `IllegalAccessError: VillagerTrades$ItemsForEmeralds` in production. It compiled only because
    /// another dependency on `common`'s classpath contributes an access widener the production runtime
    /// lacks. The raw `TradeOffer` constructor has no such problem — see `jewelry-port-notes.md §4`.
    private static TradeOffers.Factory sell(ItemConvertible item, int price, int count, int experience) {
        return sell(item, price, count, 12, experience, 0.05F);
    }

    /// Mirrors `TradeOffers.SellItemFactory(Item, price, count, maxUses, experience)`.
    private static TradeOffers.Factory sell(ItemConvertible item, int price, int count, int maxUses, int experience) {
        return sell(item, price, count, maxUses, experience, 0.05F);
    }

    /// Mirrors `TradeOffers.SellItemFactory(ItemStack, price, count, maxUses, experience, multiplier)`:
    /// the villager takes `price` emeralds and gives back `count` of the item.
    private static TradeOffers.Factory sell(ItemConvertible item, int price, int count, int maxUses, int experience, float multiplier) {
        return (Entity entity, Random random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, price), new ItemStack(item, count), maxUses, experience, multiplier);
    }

    /// Mirrors `TradeOffers.SellEnchantedToolFactory(Item, basePrice, maxUses, experience, multiplier)`,
    /// which is package-private for the same reason as `SellItemFactory` above. Vanilla's arithmetic,
    /// reproduced exactly: an enchantment level of `5 + random.nextInt(15)` (so 5..19), a non-treasure
    /// `EnchantmentHelper.enchant` at that level, and an emerald price of `min(basePrice + level, 64)`.
    private static TradeOffers.Factory sellEnchanted(Item item, int basePrice, int maxUses, int experience, float multiplier) {
        return (Entity entity, Random random) -> {
            int level = 5 + random.nextInt(15);
            ItemStack tool = EnchantmentHelper.enchant(random, new ItemStack(item), level, false);
            int price = Math.min(basePrice + level, 64);
            return new TradeOffer(new ItemStack(Items.EMERALD, price), tool, maxUses, experience, multiplier);
        };
    }
}
