package net.archers.forge.client.curios;

import net.archers.item.Quivers;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

/// Binds a {@link CuriosQuiverRenderer} to every quiver item, so an equipped quiver shows on the
/// wearer's back. Mirrors `net.archers.fabric.client.trinkets.TrinketsRenderCompat`.
///
/// `CuriosRendererRegistry.register(Item, Supplier<ICurioRenderer>)` is unchanged between Curios 5.14.1
/// and Curios 9; its javadoc asks for it to be called from `FMLClientSetupEvent` (Curios materialises the
/// suppliers later, in `EntityRenderersEvent.AddLayers`), which is where `ForgeClientMod` calls this.
public class CuriosRenderCompat {
    public static void init() {
        for (var entry : Quivers.entries) {
            CuriosRendererRegistry.register(entry.item(), () -> new CuriosQuiverRenderer(entry.id().getPath()));
        }
    }
}
