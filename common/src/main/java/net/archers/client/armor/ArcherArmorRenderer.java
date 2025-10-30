package net.archers.client.armor;

import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererConfig;
import mod.azure.azurelibarmor.common.render.layer.AzArmorTrimLayer;
import net.archers.ArchersMod;
import net.minecraft.util.Identifier;

public class ArcherArmorRenderer extends AzArmorRenderer {
    public static ArcherArmorRenderer archer() {
        return new ArcherArmorRenderer("archer_armor", "archer_armor", "archer_armor_generic");
    }
    public static ArcherArmorRenderer ranger() {
        return new ArcherArmorRenderer("ranger_armor", "ranger_armor", "ranger_armor_generic");
    }
    public static ArcherArmorRenderer netheriteRanger() {
        return new ArcherArmorRenderer("ranger_armor", "netherite_ranger_armor", "ranger_armor_generic");
    }

    public ArcherArmorRenderer(String modelName, String textureName, String trimTextureName) {
        super(AzArmorRendererConfig.builder(
                        Identifier.of(ArchersMod.ID, "geo/" + modelName + ".geo.json"),
                        Identifier.of(ArchersMod.ID, "textures/armor/" + textureName + ".png"))
                .addRenderLayer(new AzArmorTrimLayer(Identifier.of(ArchersMod.ID, "armor/trim/" + trimTextureName), false))
                .build()
        );
    }
}
