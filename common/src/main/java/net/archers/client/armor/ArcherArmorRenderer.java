package net.archers.client.armor;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import net.archers.ArchersMod;
import net.minecraft.util.Identifier;

public class ArcherArmorRenderer extends AzArmorRenderer {
    public static ArcherArmorRenderer archer() {
        return new ArcherArmorRenderer("archer_armor", "archer_armor");
    }
    public static ArcherArmorRenderer ranger() {
        return new ArcherArmorRenderer("ranger_armor", "ranger_armor");
    }
    public static ArcherArmorRenderer netheriteRanger() {
        return new ArcherArmorRenderer("ranger_armor", "netherite_ranger_armor");
    }

    public ArcherArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(ArchersMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(ArchersMod.ID, "textures/armor/" + textureName + ".png")
        ).build());
    }
}
