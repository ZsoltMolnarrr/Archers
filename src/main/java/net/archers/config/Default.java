package net.archers.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Default {
    public final static ArchersItemConfig itemConfig;
    public static final WorldGenConfig worldGen;

    static {
        itemConfig = new ArchersItemConfig();
        worldGen = new WorldGenConfig();
        var largeWeight = 1;
        var smallWeight = 3;
        worldGen.entries = new ArrayList<>(List.of(
                new WorldGenConfig.Entry("minecraft:village/desert/houses", new ArrayList<>(Arrays.asList(
                        new WorldGenConfig.Entry.Structure("archers:village/desert/archery_range_large", largeWeight),
                        new WorldGenConfig.Entry.Structure("archers:village/desert/archery_range_small", smallWeight))
                )),
                new WorldGenConfig.Entry("minecraft:village/savanna/houses", new ArrayList<>(Arrays.asList(
                        new WorldGenConfig.Entry.Structure("archers:village/savanna/archery_range_large", largeWeight),
                        new WorldGenConfig.Entry.Structure("archers:village/savanna/archery_range_small", smallWeight))
                )),
                new WorldGenConfig.Entry("minecraft:village/plains/houses", new ArrayList<>(Arrays.asList(
                        new WorldGenConfig.Entry.Structure("archers:village/plains/archery_range_large", largeWeight),
                        new WorldGenConfig.Entry.Structure("archers:village/plains/archery_range_small", smallWeight))
                )),
                new WorldGenConfig.Entry("minecraft:village/taiga/houses", new ArrayList<>(Arrays.asList(
                        new WorldGenConfig.Entry.Structure("archers:village/taiga/archery_range_large", largeWeight),
                        new WorldGenConfig.Entry.Structure("archers:village/taiga/archery_range_small", smallWeight))
                )),
                new WorldGenConfig.Entry("minecraft:village/snowy/houses", new ArrayList<>(Arrays.asList(
                        new WorldGenConfig.Entry.Structure("archers:village/snowy/archery_range_large", largeWeight),
                        new WorldGenConfig.Entry.Structure("archers:village/snowy/archery_range_small", smallWeight))
                ))
        ));

    }
}
