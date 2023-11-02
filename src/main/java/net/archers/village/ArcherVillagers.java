package net.archers.village;

import com.google.common.collect.ImmutableSet;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.item.Weapons;
import net.archers.item.Armors;
import net.archers.util.SoundHelper;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
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

import java.util.List;

public class ArcherVillagers {
    public static final String ARCHERY_ARTISAN = "archery_artisan";

    public static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(ArchersMod.ID, name),
                1, 10, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

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
                SoundHelper.WORKBENCH)
        );
    }

    private static class Offer {
        int level;
        ItemStack input;
        ItemStack output;
        int maxUses;
        int experience;
        float priceMultiplier;

        public Offer(int level, ItemStack input, ItemStack output, int maxUses, int experience, float priceMultiplier) {
            this.level = level;
            this.input = input;
            this.output = output;
            this.maxUses = maxUses;
            this.experience = experience;
            this.priceMultiplier = priceMultiplier;
        }

        public static Offer buy(int level, ItemStack item, int price, int maxUses, int experience, float priceMultiplier) {
            return new Offer(level, item, new ItemStack(Items.EMERALD, price), maxUses, experience, priceMultiplier);
        }

        public static Offer sell(int level, ItemStack item, int price, int maxUses, int experience, float priceMultiplier) {
            return new Offer(level, new ItemStack(Items.EMERALD, price), item, maxUses, experience, priceMultiplier);
        }
    }

    public static void register() {
        StructurePoolAPI.injectAll(ArchersMod.villagesConfig.value);
        var poi = registerPOI(ARCHERY_ARTISAN, ArcherBlocks.WORKBENCH.block());
        var profession = registerProfession(
                ARCHERY_ARTISAN,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), new Identifier(ArchersMod.ID, ARCHERY_ARTISAN)));

        List<Offer> offers = List.of(
                Offer.sell(1, new ItemStack(Items.ARROW, 8), 2, 128, 1, 0.01f),
                Offer.buy(1, new ItemStack(Items.LEATHER, 8), 5, 12, 4, 0.01f),
                Offer.sell(2, Weapons.composite_longbow.item().getDefaultStack(), 12, 12, 10, 0.1f),
                Offer.sell(2, Armors.archerArmorSet_T1.head.getDefaultStack(), 15, 12, 13, 0.05f),
                Offer.buy(2, new ItemStack(Items.STRING, 5), 3, 12, 4, 0.01f),
                Offer.sell(3, Armors.archerArmorSet_T1.feet.getDefaultStack(), 15, 12, 13, 0.05f),
                Offer.buy(3, new ItemStack(Items.REDSTONE, 12), 3, 12, 5, 0.01f),
                Offer.sell(3, Armors.archerArmorSet_T1.legs.getDefaultStack(), 15, 12, 13, 0.05f),
                Offer.sell(4, Armors.archerArmorSet_T1.chest.getDefaultStack(), 15, 12, 13, 0.05f),
                Offer.sell(4, new ItemStack(Items.SCUTE, 3), 20, 12, 5, 0.01f)
            );

        for(var offer: offers) {
            TradeOfferHelper.registerVillagerOffers(profession, offer.level, factories -> {
                factories.add(((entity, random) -> new TradeOffer(
                        offer.input,
                        offer.output,
                        offer.maxUses, offer.experience, offer.priceMultiplier)
                ));
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
