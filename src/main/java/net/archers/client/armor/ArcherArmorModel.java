package net.archers.client.armor;

import mod.azure.azurelibarmor.model.GeoModel;
import net.archers.ArchersMod;
import net.archers.item.armor.ArcherArmor;
import net.minecraft.util.Identifier;

public class ArcherArmorModel extends GeoModel<ArcherArmor> {
    @Override
    public Identifier getModelResource(ArcherArmor armor) {
        var name = armor.customMaterial.name();
        return new Identifier(ArchersMod.ID, "geo/" + name + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ArcherArmor armor) {
        var name = armor.customMaterial.name();
        return new Identifier(ArchersMod.ID, "textures/armor/" + name + ".png");
    }

    @Override
    public Identifier getAnimationResource(ArcherArmor animatable) {
        return null;
    }
}
