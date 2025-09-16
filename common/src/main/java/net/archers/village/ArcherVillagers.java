package net.archers.village;

import com.google.common.collect.ImmutableSet;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.Weapons;
import net.archers.item.Armors;
import net.archers.content.ArcherSounds;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.LinkedHashMap;
import java.util.List;

public class ArcherVillagers {
    public static final String ARCHERY_ARTISAN = "archery_artisan";
    public static final Identifier POI_ID = Identifier.of(ArchersMod.ID, ARCHERY_ARTISAN);

    public static void registerPOI() {
        var blockStates = ImmutableSet.copyOf(ArcherBlocks.WORKBENCH.block().getStateManager().getStates());
        PointOfInterestHelper.register(POI_ID, 1, 10, blockStates);
    }

    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = Identifier.of(ArchersMod.ID, name);
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(ArchersMod.ID, name), new VillagerProfession(
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

//    private static class Offer {
//        int level;
//        ItemStack input;
//        ItemStack output;
//        int maxUses;
//        int experience;
//        float priceMultiplier;
//
//        public Offer(int level, ItemStack input, ItemStack output, int maxUses, int experience, float priceMultiplier) {
//            this.level = level;
//            this.input = input;
//            this.output = output;
//            this.maxUses = maxUses;
//            this.experience = experience;
//            this.priceMultiplier = priceMultiplier;
//        }
//
//        public static Offer buy(int level, ItemStack component, int price, int maxUses, int experience, float priceMultiplier) {
//            return new Offer(level, component, new ItemStack(Items.EMERALD, price), maxUses, experience, priceMultiplier);
//        }
//
//        public static Offer sell(int level, ItemStack component, int price, int maxUses, int experience, float priceMultiplier) {
//            return new Offer(level, new ItemStack(Items.EMERALD, price), component, maxUses, experience, priceMultiplier);
//        }
//    }

    public static void registerVillagers() {
        if (!FabricLoader.getInstance().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(ArchersMod.villagesConfig.value);
        }
        var profession = registerProfession(
                ARCHERY_ARTISAN,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), POI_ID));

//        List<Offer> offers = List.of(
//                Offer.sell(1, new ItemStack(Items.ARROW, 8), 2, 128, 1, 0.01f),
//                Offer.buy(1, new ItemStack(Items.LEATHER, 8), 5, 12, 4, 0.01f),
//                Offer.sell(2, Weapons.composite_longbow.component().getDefaultStack(), 12, 12, 10, 0.1f),
//                Offer.sell(2, Armors.archerArmorSet_T1.head.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.buy(2, new ItemStack(Items.STRING, 5), 3, 12, 4, 0.01f),
//                Offer.sell(3, Armors.archerArmorSet_T1.feet.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.buy(3, new ItemStack(Items.REDSTONE, 12), 3, 12, 5, 0.01f),
//                Offer.sell(3, Armors.archerArmorSet_T1.legs.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(4, Armors.archerArmorSet_T1.chest.getDefaultStack(), 15, 12, 13, 0.05f),
//                Offer.sell(4, new ItemStack(Items.TURTLE_SCUTE, 3), 20, 12, 5, 0.01f)
//            );

        LinkedHashMap<Integer, List<TradeOffers.Factory>> trades = new LinkedHashMap<>();

        trades.put(1, List.of(
                new TradeOffers.SellItemFactory(Items.ARROW, 2, 8, 128, 3, 0.01f),
                new TradeOffers.BuyItemFactory(Items.LEATHER, 8, 12, 6, 5)
        ));
        trades.put(2, List.of(
                new TradeOffers.SellItemFactory(Weapons.composite_longbow.item(), 6, 1, 16),
                new TradeOffers.SellItemFactory(Armors.archerArmorSet_T1.head, 15, 1, 18),
                new TradeOffers.BuyItemFactory(Items.STRING, 6, 12, 8, 3)
        ));
        trades.put(3, List.of(
                new TradeOffers.SellItemFactory(Armors.archerArmorSet_T1.feet, 15, 1, 18),
                new TradeOffers.BuyItemFactory(Items.REDSTONE, 12, 12, 5, 8),
                new TradeOffers.SellItemFactory(Armors.archerArmorSet_T1.legs, 15, 1, 18)
        ));
        trades.put(4, List.of(
                new TradeOffers.SellItemFactory(Armors.archerArmorSet_T1.chest, 15, 1, 18),
                new TradeOffers.SellItemFactory(Items.TURTLE_SCUTE, 20, 12, 10)
        ));

        for (var entry: trades.entrySet()) {
            TradeOfferHelper.registerVillagerOffers(profession, entry.getKey(), factories -> {
                factories.addAll(entry.getValue());
            });
        }

        TradeOfferHelper.registerVillagerOffers(profession, 5, factories -> {
            factories.add(((entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                    Weapons.royal_longbow.item(),
                    40,
                    3,
                    30,
                    0F).create(entity, random)
            ));
            factories.add(((entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                    Weapons.mechanic_shortbow.item(),
                    40,
                    3,
                    30,
                    0F).create(entity, random)
            ));
            factories.add(((entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                    Weapons.rapid_crossbow.item(),
                    40,
                    3,
                    30,
                    0F).create(entity, random)
            ));
            factories.add(((entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                    Weapons.heavy_crossbow.item(),
                    40,
                    3,
                    30,
                    0F).create(entity, random)
            ));
        });
    }
}
