package net.archers.item;

import net.archers.ArchersMod;
import net.archers.item.armor.ArcherArmor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.archers.content.ArcherSounds;
import net.rpg_foundation.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.item.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ArcherArmors {
    /// Item tag listing the items that repair this armor set (1.21.2+ replaced the
    /// `Supplier<Ingredient>` repair ingredient with a `TagKey<Item>`).
    public static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ArchersMod.ID, "repairs_" + name));
    }

    /// Equipment asset key of the vanilla armor layer. Archers armor is drawn by the Armor Model API
    /// (geo model + own texture), so no `assets/archers/equipment/<name>.json` is shipped and the
    /// vanilla layer resolves to `EquipmentModelLoader.EMPTY` — same net effect as the 1.21.1
    /// `ArmorMaterial.Layer` that pointed at a texture we never shipped.
    public static ResourceKey<EquipmentAsset> assetKey(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ArchersMod.ID, name));
    }

    public static ArmorMaterial material(String name,
                                         int durability,
                                         int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
                                         int enchantability, Holder<SoundEvent> equipSound) {
        return new ArmorMaterial(
                durability,
                Map.of(
                        ArmorType.HELMET, protectionHead,
                        ArmorType.CHESTPLATE, protectionChest,
                        ArmorType.LEGGINGS, protectionLegs,
                        ArmorType.BOOTS, protectionFeet),
                enchantability,
                equipSound,
                0F, 0F,
                repairTag(name),
                assetKey(name)
        );
    }

    public static ArmorMaterial material_t1 = material(
            "archer_armor",
            15,
            2, 3, 3, 2,
            9,
            ArcherSounds.ARCHER_ARMOR_EQUIP.entry());

    public static ArmorMaterial material_t2 = material(
            "ranger_armor",
            25,
            2, 3, 3, 2,
            10,
            ArcherSounds.ARCHER_ARMOR_EQUIP.entry());

    public static ArmorMaterial material_t3 = material(
            "netherite_ranger_armor",
            35,
            2, 3, 3, 2,
            15,
            ArcherSounds.ARCHER_ARMOR_EQUIP.entry());


    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(ArmorMaterial material, Identifier id, int durability, Armor.Set.ItemFactory factory, ArmorSetConfig defaults, int tier) {
        var entry = Armor.Entry.create(
                material,
                id,
                durability,
                factory,
                defaults,
                Equipment.LootProperties.of(tier)
        );
        entries.add(entry);
        return entry;
    }

    private static AttributeModifier damageMultiplier(float value) {
        return new AttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                value,
                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    private static AttributeModifier hasteMultiplier(float value) {
        return new AttributeModifier(
                EntityAttributes_RangedWeapon.HASTE.id.toString(),
                value,
                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    public static final float damage_T1 = 0.05F;
    public static final float haste_T2 = 0.03F;
    public static final float damage_T2 = 0.08F;
    public static final float haste_T3 = 0.04F;
    public static final float damage_T3 = 0.09F;

    public static final Armor.Set archerArmorSet_T1 = create(
            material_t1,
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "archer_armor"),
            15,
            ArcherArmor::archer,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T1)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T1)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T1)),
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T1))
            ),
            1)
            .translatedName("Archer Hood", "Archer Tunic", "Archer Leggings", "Archer Boots")
            .armorSet();

    public static final Armor.Set archerArmorSet_T2 = create(
            material_t2,
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "ranger_armor"),
            25,
            ArcherArmor::ranger,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T2))
                            .add(hasteMultiplier(haste_T2)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T2))
                            .add(hasteMultiplier(haste_T2)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T2))
                            .add(hasteMultiplier(haste_T2)),
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T2))
                            .add(hasteMultiplier(haste_T2))
            ),
            2)
            .translatedName("Ranger Hood", "Ranger Tunic", "Ranger Leggings", "Ranger Boots")
            .armorSet();

    public static final Armor.Set archerArmorSet_T3 = create(
            material_t3,
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "netherite_ranger_armor"),
            35,
            ArcherArmor::ranger,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T3))
                            .add(hasteMultiplier(haste_T3)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T3))
                            .add(hasteMultiplier(haste_T3)),
                    new ArmorSetConfig.Piece(3)
                            .add(damageMultiplier(damage_T3))
                            .add(hasteMultiplier(haste_T3)),
                    new ArmorSetConfig.Piece(2)
                            .add(damageMultiplier(damage_T3))
                            .add(hasteMultiplier(haste_T3))
            )
            , 3)
            .translatedName("Netherite Ranger Hood", "Netherite Ranger Tunic", "Netherite Ranger Leggings", "Netherite Ranger Boots")
            .armorSet();

    public static void register(Map<String, ArmorSetConfig> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}

