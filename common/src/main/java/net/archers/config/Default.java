package net.archers.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ArchersItemConfig itemConfig;
    public final static StructurePoolConfig villageConfig;

    static {
        itemConfig = new ArchersItemConfig();

        villageConfig = new StructurePoolConfig();
        // The large ranges ship at weight 0 (disabled by default); the config file lets players raise it.
        var largeWeight = 0;
        var smallWeight = 5;
        var limit = 1;
        villageConfig.entries = new ArrayList<>(List.of(
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
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
