package net.archers.item;

import net.archers.ArchersMod;
import net.archers.item.armor.ArcherArmor;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;

import java.util.ArrayList;
import java.util.Map;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }

    private static ItemConfig.Attribute damageMultiplier(float value) {
        return new ItemConfig.Attribute(
                EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                value,
                EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    private static ItemConfig.Attribute hasteMultiplier(float value) {
        return new ItemConfig.Attribute(
                EntityAttributes_RangedWeapon.HASTE.id.toString(),
                value,
                EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static final float damage_T1 = 0.05F;
    public static final float haste_T2 = 0.03F;
    public static final float damage_T2 = 0.08F;
    public static final float damage_T3 = 0.10F;
    public static final float haste_T3 = 0.05F;

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
                                .add(damageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(3)
                                .add(damageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(3)
                                .add(damageMultiplier(damage_T1)),
                        new ItemConfig.ArmorSet.Piece(2)
                                .add(damageMultiplier(damage_T1))
                    )
            )
            .bundle(material -> new Armor.Set(ArchersMod.ID,
                    new ArcherArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .armorSet();

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
                                    .add(damageMultiplier(damage_T2))
                                    .add(hasteMultiplier(haste_T2)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(damageMultiplier(damage_T2))
                                    .add(hasteMultiplier(haste_T2)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(damageMultiplier(damage_T2))
                                    .add(hasteMultiplier(haste_T2)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .add(damageMultiplier(damage_T2))
                                    .add(hasteMultiplier(haste_T2))
                    )
            )
            .bundle(material -> new Armor.Set(ArchersMod.ID,
                    new ArcherArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .armorSet();

	public static final Armor.Set archerArmorSet_T3 =
            create(
                    new Armor.CustomMaterial(
                            "netherite_ranger_armor",
                            30,
                            15,
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                            () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); }
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .add(damageMultiplier(damage_T3))
                                    .add(hasteMultiplier(haste_T3)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(damageMultiplier(damage_T3))
                                    .add(hasteMultiplier(haste_T3)),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .add(damageMultiplier(damage_T3))
                                    .add(hasteMultiplier(haste_T3)),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .add(damageMultiplier(damage_T3))
                                    .add(hasteMultiplier(haste_T3))
                    )
            )
            .bundle(material -> new Armor.Set(ArchersMod.ID,
                    new ArcherArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                    new ArcherArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
            ))
            .put(entries)
            .armorSet();

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}

