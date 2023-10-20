package net.archers.item;

import net.archers.ArchersMod;
import net.archers.config.RangedConfig;
import net.archers.item.weapon.CustomBow;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Weapons {
    public static final ArrayList<Entry> all = new ArrayList<>();
    public record Entry(Identifier id, CustomBow item, RangedConfig defaults) { }
    private static Entry add(Identifier id, CustomBow item, RangedConfig defaults) {
        var entry = new Entry(id, item, defaults);
        item.configure(defaults);
        all.add(entry);
        return entry;
    }

    private static final int durabilityTier1 = 384;
    private static final int durabilityTier2 = ToolMaterials.IRON.getDurability();
    private static final int durabilityTier3 = ToolMaterials.DIAMOND.getDurability();
    private static final int durabilityTier4 = ToolMaterials.NETHERITE.getDurability();

    public static Entry mechanic_shortbow = add(
        new Identifier(ArchersMod.ID, "mechanic_shortbow"),
        new CustomBow(new FabricItemSettings().maxDamage(durabilityTier2), () -> Ingredient.ofItems(Items.IRON_INGOT)),
        new RangedConfig(20, 4, 1.5F)
    );

    public static void register() {
        for (var entry: all) {
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register((content) -> {
            for (var entry: all) {
                content.add(entry.item);
            }
        });
    }
}
