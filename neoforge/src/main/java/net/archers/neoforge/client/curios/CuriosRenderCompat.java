package net.archers.neoforge.client.curios;

import net.archers.item.Quivers;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRenderCompat {
    public static void init() {
        for (var entry : Quivers.entries) {
            CuriosRendererRegistry.register(entry.item(), () -> new CuriosQuiverRenderer(entry.id().getPath()));
        }
    }
}
