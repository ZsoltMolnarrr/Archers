package net.archers.item;

import net.archers.ArchersMod;
import net.archers.item.Group;
import net.archers.item.armor.ArcherArmor;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.projectile_damage.api.EntityAttributes_ProjectileDamage;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;

import java.util.ArrayList;
import java.util.Map;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }

    private static ItemConfig.Attribute projectileDamageMultiplier(float value) {
        return new ItemConfig.Attribute(
                EntityAttributes_ProjectileDamage.attributeId.toString(),
                value,
                EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static final float damage_T1 = 0.05F;
    public static final float damage_T2 = 0.1F;

    public static final Armor.Set archerArmorSet_T1 =
            create(
                    new Armor.CustomMaterial(
                        "archer_armor",
                        10,
                        9,
                        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                        () -> { return Ingredient.ofItems(Items.LEATHER); }
                    ),
                    ItemConfig.ArmorSet.with(
                        new ItemConfig.ArmorSet.Piece(2)
                                .add(projectileDamageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(3)
                                .add(projectileDamageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(3)
                                .add(projectileDamageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(2)
                                .add(projectileDamageMultiplier(damage_T1))
                    )
            )
            .bundle(material -> new Armor.Set(ArchersMod.ID,
                    new ArcherArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .armorSet()
            .allowSpellPowerEnchanting(false);


    public static final Armor.Set archerArmorSet_T2 =
            create(
                    new Armor.CustomMaterial(
                            "ranger_armor",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                            () -> { return Ingredient.ofItems(Items.SCUTE); }
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .add(projectileDamageMultiplier(damage_T2)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(projectileDamageMultiplier(damage_T2)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(projectileDamageMultiplier(damage_T2)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .add(projectileDamageMultiplier(damage_T2))
                    )
            )
            .bundle(material -> new Armor.Set(ArchersMod.ID,
                    new ArcherArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .armorSet()
            .allowSpellPowerEnchanting(false);

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}

