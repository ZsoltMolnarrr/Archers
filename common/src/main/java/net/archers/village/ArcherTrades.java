package net.archers.village;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;

import java.util.Optional;

/// Trade-offer factories for the archery artisan.
///
/// Vanilla's `TradeOffers.SellItemFactory` / `BuyItemFactory` / `SellEnchantedToolFactory` became
/// package-private in 1.21.11 (Fabric API widens them transitively, NeoForge does not), so common code
/// can no longer reference them on both loaders. These are behaviour-identical re-implementations over
/// the public [TradeOffer] / [TradedItem] API.
public final class ArcherTrades {
    private ArcherTrades() { }

    /// Villager sells `count` x `item` for `price` emeralds.
    public record Sell(Item item, int price, int count, int maxUses, int experience, float multiplier) implements TradeOffers.Factory {
        public Sell(Item item, int price, int count, int experience) {
            this(item, price, count, 12, experience, 0.05F);
        }
        public Sell(Item item, int price, int count, int maxUses, int experience) {
            this(item, price, count, maxUses, experience, 0.05F);
        }

        @Override
        public TradeOffer create(ServerWorld world, Entity entity, Random random) {
            var sold = new ItemStack(item);
            sold.setCount(count);
            return new TradeOffer(new TradedItem(Items.EMERALD, price), sold, maxUses, experience, multiplier);
        }
    }

    /// Villager buys `count` x `item` for `price` emeralds.
    public record Buy(ItemConvertible item, int count, int maxUses, int experience, int price) implements TradeOffers.Factory {
        @Override
        public TradeOffer create(ServerWorld world, Entity entity, Random random) {
            return new TradeOffer(new TradedItem(item.asItem(), count),
                    new ItemStack(Items.EMERALD, price), maxUses, experience, 0.05F);
        }
    }

    /// Villager sells a randomly enchanted `item`; the emerald price rises with the enchantment level,
    /// exactly as vanilla's tool trades do.
    public record SellEnchanted(Item item, int basePrice, int maxUses, int experience, float multiplier) implements TradeOffers.Factory {
        @Override
        public TradeOffer create(ServerWorld world, Entity entity, Random random) {
            int level = 5 + random.nextInt(15);
            var registryManager = world.getRegistryManager();
            Optional<RegistryEntryList.Named<Enchantment>> onTradedEquipment = registryManager
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getOptional(EnchantmentTags.ON_TRADED_EQUIPMENT);
            var enchanted = EnchantmentHelper.enchant(random, new ItemStack(item), level, registryManager, onTradedEquipment);
            int price = Math.min(basePrice + level, 64);
            return new TradeOffer(new TradedItem(Items.EMERALD, price), enchanted, maxUses, experience, multiplier);
        }
    }
}
