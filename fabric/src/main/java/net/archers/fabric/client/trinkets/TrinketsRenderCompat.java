package net.archers.fabric.client.trinkets;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.archers.item.Quivers;

public class TrinketsRenderCompat {
    public static void init() {
        for (var entry: Quivers.entries) {
            TrinketRendererRegistry.registerRenderer(entry.item(), new TrinketsQuiverRenderer(entry.id().getPath()));
        }
    }
}
