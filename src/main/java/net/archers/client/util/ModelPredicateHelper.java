package net.archers.client.util;

import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomCrossbow;
import net.archers.item.weapon.CustomRangedWeaponProperties;
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
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / ((float) ((CustomRangedWeaponProperties)bow).getCustomPullTime_RPGS());
            }
        });
        ModelPredicateProviderRegistry.register(bow, new Identifier("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }

    public static void registerCrossbowModelPredicates(CustomCrossbow crossbow) {
        var predicatesToCopy = new Identifier[] {
                new Identifier("pull"),
                new Identifier("pulling"),
                new Identifier("charged"),
                new Identifier("firework")
        };
        for (var predicateId : predicatesToCopy) {
            var predicateProvider = ModelPredicateProviderRegistry.get(Items.CROSSBOW, predicateId);
            ModelPredicateProviderRegistry.register(crossbow, predicateId, (stack, world, entity, seed) -> {
                return predicateProvider.call(stack, world, entity, seed);
            });
        }
    }
}
