package net.archers.neoforge.client.curios;

import net.archers.item.Quivers;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosRenderCompat {
    public static void init() {
        for (var entry : Quivers.entries) {
            // Curios 14: `CuriosRendererRegistry` is deprecated for removal in favour of ICurioRenderer.register
            ICurioRenderer.register(entry.item(), () -> new CuriosQuiverRenderer(entry.id().getPath()));
        }
    }
}
