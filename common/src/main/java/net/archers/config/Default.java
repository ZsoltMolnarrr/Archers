package net.archers.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static ArchersItemConfig itemConfig;

    // The vanilla-village archery-range pool config (`config/archers/villages.json`) moved to
    // `net.archers.fabric.village.FabricVillageStructures` — StructurePoolAPI is Fabric-only on 1.20.1.

    static {
        itemConfig = new ArchersItemConfig();
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
