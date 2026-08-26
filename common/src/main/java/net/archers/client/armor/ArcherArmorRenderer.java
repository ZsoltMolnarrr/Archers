package net.archers.client.armor;

import net.archers.ArchersMod;
import net.minecraft.resources.Identifier;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;

public final class ArcherArmorRenderer {
    private ArcherArmorRenderer() { }

    public static GeoArmorRenderer archer() {
        return make("archer_armor", "archer_armor", "archer_armor_generic");
    }
    public static GeoArmorRenderer ranger() {
        return make("ranger_armor", "ranger_armor", "ranger_armor_generic");
    }
    public static GeoArmorRenderer netheriteRanger() {
        return make("ranger_armor", "netherite_ranger_armor", "ranger_armor_generic");
    }

    private static GeoArmorRenderer make(String modelName, String textureName, String trimTextureName) {
        return GeoArmorRenderer.of(
                Identifier.fromNamespaceAndPath(ArchersMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.fromNamespaceAndPath(ArchersMod.ID, "textures/armor/" + textureName + ".png"))
                .trim(Identifier.fromNamespaceAndPath(ArchersMod.ID, "armor/trim/" + trimTextureName), false);
    }
}
