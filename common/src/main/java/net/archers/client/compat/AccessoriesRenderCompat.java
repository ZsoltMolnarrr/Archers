package net.archers.client.compat;

import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.archers.item.Quivers;

public class AccessoriesRenderCompat {
    public static void init() {
        for (var entry: Quivers.entries) {
            AccessoriesRendererRegistry.registerRenderer(entry.item(), () -> new AccessoriesQuiverRenderer(entry.id().getPath()));
        }
    }
}
