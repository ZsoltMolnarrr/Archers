package net.archers.component;

import net.archers.ArchersMod;
import net.minecraft.item.ItemStack;

/// 1.20.1 has no data components, so the Auto-Fire Hook flag lives in the stack's NBT under the
/// legacy key {@code archers:afh} — the exact key the pre-1.21 Archers releases used, so hooked
/// crossbows from those worlds keep working.
public class ArcherComponents {
    public static final String AUTO_FIRE_NBT_KEY = ArchersMod.ID + ":afh";

    public static boolean getAutoFire(ItemStack stack) {
        var nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean(AUTO_FIRE_NBT_KEY);
    }

    public static void setAutoFire(ItemStack stack, boolean value) {
        stack.getOrCreateNbt().putBoolean(AUTO_FIRE_NBT_KEY, value);
    }

    public static void removeAutoFire(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt != null) {
            nbt.remove(AUTO_FIRE_NBT_KEY);
            if (nbt.isEmpty()) {
                stack.setNbt(null);
            }
        }
    }
}
