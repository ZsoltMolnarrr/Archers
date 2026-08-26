package net.archers.item;

import net.archers.ArchersMod;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/// Anvil-repair item tags, one per *material*, shared by every item repaired with it.
///
/// `Item.Settings.repairable(TagKey)` binds a live tag handle, so contents are read at anvil time:
/// a cross-mod item simply needs an optional entry (`required: false`) and works whenever that mod is
/// present, and datapacks can override any of these. Where a vanilla tag already holds exactly the right
/// item (`ItemTags.NETHERITE_TOOL_MATERIALS`, ...) it is used directly instead of a tag defined here.
///
/// Melee weapons pass `null` when the repair material is the tier's own `ToolMaterial` material — that
/// keeps the vanilla tag (iron ingot for T1, diamond for T2, netherite ingot for T3, gold ingot for GOLDEN).
///
/// Contents are emitted by `ArchersDataGenerator.ItemTagGenerator`.
public class ArcherItemTags {
    public record RepairTag(TagKey<Item> tag, List<Identifier> required, List<Identifier> optional) {}

    public static final List<RepairTag> REPAIR_TAGS = new ArrayList<>();

    private static TagKey<Item> repairs(String material, List<Identifier> required, List<Identifier> optional) {
        var tag = TagKey.of(RegistryKeys.ITEM, Identifier.of(ArchersMod.ID, "repairs_" + material));
        REPAIR_TAGS.add(new RepairTag(tag, required, optional));
        return tag;
    }

    private static TagKey<Item> repairs(String material, Identifier... required) {
        return repairs(material, List.of(required), List.of());
    }

    /// Cross-mod material: the foreign item is an optional entry, the vanilla item keeps the weapon
    /// repairable when that mod is absent.
    private static TagKey<Item> repairsModded(String material, String moddedItem, String vanillaFallback) {
        return repairs(material, List.of(Identifier.ofVanilla(vanillaFallback)), List.of(Identifier.of(moddedItem)));
    }

    public static final TagKey<Item> REPAIRS_FLINT = repairs("flint", Identifier.ofVanilla("flint"));
    public static final TagKey<Item> REPAIRS_BONE = repairs("bone", Identifier.ofVanilla("bone"));
    public static final TagKey<Item> REPAIRS_REDSTONE = repairs("redstone", Identifier.ofVanilla("redstone"));

    public static final TagKey<Item> REPAIRS_AETERNIUM = repairsModded("aeternium", "betterend:aeternium_ingot", "netherite_ingot");
    public static final TagKey<Item> REPAIRS_CRYSTAL_SHARDS = repairsModded("crystal_shards", "betterend:crystal_shards", "netherite_ingot");
    public static final TagKey<Item> REPAIRS_NETHER_RUBY = repairsModded("nether_ruby", "betternether:nether_ruby", "netherite_ingot");
    public static final TagKey<Item> REPAIRS_AMBROSIUM = repairsModded("ambrosium", "aether:ambrosium_shard", "netherite_ingot");
}
