package net.archers.village;

import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

/// Trade-offer factories for the archery artisan.
///
/// Vanilla's `TradeOffers.SellItemFactory` / `BuyItemFactory` / `SellEnchantedToolFactory` became
/// package-private in 1.21.11 (Fabric API widens them transitively, NeoForge does not), so common code
/// can no longer reference them on both loaders. These are behaviour-identical re-implementations over
/// the public [TradeOffer] / [TradedItem] API.
public final class ArcherTrades {
    private ArcherTrades() { }

    /// Villager sells `count` x `item` for `price` emeralds.
    public record Sell(Item item, int price, int count, int maxUses, int experience, float multiplier) implements VillagerTrades.ItemListing {
        public Sell(Item item, int price, int count, int experience) {
            this(item, price, count, 12, experience, 0.05F);
        }
        public Sell(Item item, int price, int count, int maxUses, int experience) {
            this(item, price, count, maxUses, experience, 0.05F);
        }

        @Override
        public MerchantOffer getOffer(ServerLevel world, Entity entity, RandomSource random) {
            var sold = new ItemStack(item);
            sold.setCount(count);
            return new MerchantOffer(new ItemCost(Items.EMERALD, price), sold, maxUses, experience, multiplier);
        }
    }

    /// Villager buys `count` x `item` for `price` emeralds.
    public record Buy(ItemLike item, int count, int maxUses, int experience, int price) implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(ServerLevel world, Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemCost(item.asItem(), count),
                    new ItemStack(Items.EMERALD, price), maxUses, experience, 0.05F);
        }
    }

    /// Villager sells a randomly enchanted `item`; the emerald price rises with the enchantment level,
    /// exactly as vanilla's tool trades do.
    public record SellEnchanted(Item item, int basePrice, int maxUses, int experience, float multiplier) implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(ServerLevel world, Entity entity, RandomSource random) {
            int level = 5 + random.nextInt(15);
            var registryManager = world.registryAccess();
            Optional<HolderSet.Named<Enchantment>> onTradedEquipment = registryManager
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(EnchantmentTags.ON_TRADED_EQUIPMENT);
            var enchanted = EnchantmentHelper.enchantItem(random, new ItemStack(item), level, registryManager, onTradedEquipment);
            int price = Math.min(basePrice + level, 64);
            return new MerchantOffer(new ItemCost(Items.EMERALD, price), enchanted, maxUses, experience, multiplier);
        }
    }
}
