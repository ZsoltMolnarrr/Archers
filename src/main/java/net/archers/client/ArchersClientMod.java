package net.archers.client;

import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomCrossbow;
import net.fabricmc.api.ClientModInitializer;

public class ArchersClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var bow: CustomBow.instances) {
            ModelPredicateHelper.registerBowModelPredicates(bow);
        }
        for (var crossbow: CustomCrossbow.instances) {
            ModelPredicateHelper.registerCrossbowModelPredicates(crossbow);
        }
        ArchersTooltip.init();
    }
}
