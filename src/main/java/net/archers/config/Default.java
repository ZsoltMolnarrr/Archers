package net.archers.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Default {
    public final static ArchersItemConfig itemConfig;
    public static final StructurePoolConfig villages;

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
    }
}
