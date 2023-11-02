package net.archers.config;

import net.archers.item.Armors;
import net.archers.item.Weapons;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.spell_engine.api.loot.LootConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ArchersItemConfig itemConfig;
    public static final StructurePoolConfig villages;
    public final static LootConfig lootConfig;

    static {
        itemConfig = new ArchersItemConfig();
        villages = new StructurePoolConfig();
        var largeWeight = 5;
        var smallWeight = 5;
        var limit = 1;
        villages.entries = new ArrayList<>(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("archers:village/desert/archery_range_large", largeWeight, limit),
                        new StructurePoolConfig.Entry.Structure("archers:village/desert/archery_range_small", smallWeight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("archers:village/savanna/archery_range_large", largeWeight, limit),
                        new StructurePoolConfig.Entry.Structure("archers:village/savanna/archery_range_small", smallWeight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("archers:village/plains/archery_range_large", largeWeight, limit),
                        new StructurePoolConfig.Entry.Structure("archers:village/plains/archery_range_small", smallWeight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("archers:village/taiga/archery_range_large", largeWeight, limit),
                        new StructurePoolConfig.Entry.Structure("archers:village/taiga/archery_range_small", smallWeight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("archers:village/snowy/archery_range_large", largeWeight, limit),
                        new StructurePoolConfig.Entry.Structure("archers:village/snowy/archery_range_small", smallWeight, limit))
                ))
        ));

        lootConfig = new LootConfig();
        final var weapons_tier_1 = "weapons_tier_1";
        lootConfig.item_groups.put(weapons_tier_1, new LootConfig.ItemGroup(List.of(
                Weapons.composite_longbow.id().toString()),
                1
        ).chance(0.1F));
        final var weapons_tier_2 = "weapons_tier_2";
        lootConfig.item_groups.put(weapons_tier_2, new LootConfig.ItemGroup(List.of(
                Weapons.mechanic_shortbow.id().toString(),
                Weapons.royal_longbow.id().toString(),
                Weapons.rapid_crossbow.id().toString(),
                Weapons.heavy_crossbow.id().toString()),
                1
        ).chance(0.3F));
        final var weapons_tier_2_enchanted = "weapons_tier_2_enchanted";
        lootConfig.item_groups.put(weapons_tier_2_enchanted, new LootConfig.ItemGroup(
                new ArrayList(lootConfig.item_groups.get(weapons_tier_2).ids),
                1
        ).chance(0.3F).enchant());

        final var armor_tier_1 = "armor_tier_1";
        lootConfig.item_groups.put(armor_tier_1, new LootConfig.ItemGroup(joinLists(
                Armors.archerArmorSet_T1.idStrings()),
                1
        ).chance(0.25F));
        final var armor_tier_1_enchanted = "armor_tier_1_enchanted";
        lootConfig.item_groups.put(armor_tier_1_enchanted, new LootConfig.ItemGroup(
                new ArrayList(lootConfig.item_groups.get(armor_tier_1).ids),
                1
        ).chance(0.25F).enchant());

        final var armor_tier_2 = "armor_tier_2";
        lootConfig.item_groups.put(armor_tier_2, new LootConfig.ItemGroup(joinLists(
                Armors.archerArmorSet_T2.idStrings()),
                1
        ).chance(0.5F));

        
        List.of("minecraft:chests/desert_pyramid",
                        "minecraft:chests/bastion_bridge",
                        "minecraft:chests/jungle_temple",
                        "minecraft:chests/pillager_outpost",
                        "minecraft:chests/underwater_ruin_small",
                        "minecraft:chests/stronghold_crossing")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_tier_1)));

        List.of("minecraft:chests/bastion_other",
                        "minecraft:chests/nether_bridge",
                        "minecraft:chests/underwater_ruin_small")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_tier_2)));

        List.of("minecraft:chests/shipwreck_supply",
                        "minecraft:chests/stronghold_corridor")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(armor_tier_1)));

        List.of("minecraft:chests/stronghold_library",
                        "minecraft:chests/underwater_ruin_big",
                        "minecraft:chests/bastion_other",
                        "minecraft:chests/woodland_mansion",
                        "minecraft:chests/simple_dungeon",
                        "minecraft:chests/underwater_ruin_big.json")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_tier_2, armor_tier_1_enchanted)));

        List.of("minecraft:chests/end_city_treasure",
                        "minecraft:chests/bastion_treasure",
                        "minecraft:chests/ancient_city",
                        "minecraft:chests/stronghold_library")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of(weapons_tier_2_enchanted, armor_tier_2)));
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
