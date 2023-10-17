package net.archers.client;

import net.archers.item.CustomBow;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ModelPredicateHelper {
    public static void registerBowModelPredicates(CustomBow bow) {
        // We cannot reuse what is already registered for Vanilla bow, because it uses hardcoded pull time values
        ModelPredicateProviderRegistry.register(bow, new Identifier("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / ((float)bow.pullTime);
            }
        });
        ModelPredicateProviderRegistry.register(bow, new Identifier("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }

    public static void registerCrossbowModelPredicates(Item item) {
        // We cannot reuse what is already registered for Vanilla bow, because it uses static pull time calculation
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(stack); // FIXME
            }
        });
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("charged"), (stack, world, entity, seed) -> {
            return CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("firework"), (stack, world, entity, seed) -> {
            return CrossbowItem.isCharged(stack) && CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }
}
