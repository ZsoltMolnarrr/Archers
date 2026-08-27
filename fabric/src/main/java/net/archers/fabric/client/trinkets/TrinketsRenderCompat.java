package net.archers.fabric.client.trinkets;

import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import net.archers.item.Quivers;

public class TrinketsRenderCompat {
    public static void init() {
        for (var entry: Quivers.entries) {
            TrinketRendererRegistry.registerRenderer(entry.item(), new TrinketsQuiverRenderer());
        }
    }
}
