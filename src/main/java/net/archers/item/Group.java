package net.archers.item;

import net.archers.ArchersMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Group {
    public static Identifier ID = new Identifier(ArchersMod.ID, "generic");
    public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), ID);
    public static ItemGroup ARCHERS;
}
