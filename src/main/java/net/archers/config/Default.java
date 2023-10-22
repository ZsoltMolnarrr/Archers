package net.archers.config;

import java.util.ArrayList;
import java.util.List;

public class Default {
    public final static ArchersItemConfig itemConfig;
    public static final WorldGenConfig worldGen;

    static {
        itemConfig = new ArchersItemConfig();
        worldGen = new WorldGenConfig();
        var structureWeight = 100;
        worldGen.entries = new ArrayList<>(List.of(
                new WorldGenConfig.Entry("minecraft:village/desert/houses", "archers:village/desert/archery_range", structureWeight),
                new WorldGenConfig.Entry("minecraft:village/savanna/houses", "archers:village/savanna/archery_range", structureWeight),
                new WorldGenConfig.Entry("minecraft:village/plains/houses", "archers:village/plains/archery_range", structureWeight),
                new WorldGenConfig.Entry("minecraft:village/taiga/houses", "archers:village/taiga/archery_range", structureWeight),
                new WorldGenConfig.Entry("minecraft:village/snowy/houses", "archers:village/snowy/archery_range", structureWeight)
        ));

    }
}
