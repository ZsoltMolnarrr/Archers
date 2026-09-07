package net.archers.forge.compat.curios;

import net.archers.item.Quivers;

/// Installs the Curios-aware quiver item class through the {@link Quivers#factory} seam.
///
/// Quivers are plain BundleAPI items, so Curios has no capability to ask about them and equipping one
/// is silent. Curios 5.x auto-attaches its `ICurio` capability to any stack whose *item* implements
/// `ICurioItem`, so the hook has to be part of the item class — {@link QuiverCurioItem}.
///
/// **Must be called before `Quivers.register()`** (i.e. before `ArchersMod.registerItems()` runs inside
/// the `ITEM` `RegisterEvent` window), and only when Curios is actually present: this class references
/// `top.theillusivec4.curios.api.*` transitively through {@link QuiverCurioItem}, so touching it without
/// Curios on the classpath is a `NoClassDefFoundError`.
public class QuiverCurios {
    public static void installFactory() {
        Quivers.factory = args -> new QuiverCurioItem(args.tag(), args.capacity(), args.settings());
    }
}
