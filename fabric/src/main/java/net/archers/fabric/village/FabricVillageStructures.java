package net.archers.fabric.village;

import net.archers.ArchersMod;
import net.archers.village.VillageStructures;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.spell_engine.Platform;
import net.tiny_config.ConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// Fabric-only implementation of {@link VillageStructures}: StructurePoolAPI has no Forge artifact on
/// 1.20.1, so both the `config/archers/villages.json` config and the injection call live here.
public final class FabricVillageStructures {
    private FabricVillageStructures() { }

    public static final ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<StructurePoolConfig>
            ("villages", defaults())
            .builder()
            .setDirectory(ArchersMod.ID)
            .sanitize(true)
            .build();

    /// Installs the injector and loads (or writes) the config file. Called from the Fabric entrypoint
    /// before {@code ArchersMod.init()}, which triggers the injection.
    public static void install() {
        villageConfig.refresh();
        VillageStructures.injector = () -> {
            if (!Platform.util().isModLoaded("lithostitched")) {
                // Only inject the village if Lithostitched is not present
                StructurePoolAPI.injectAll(villageConfig.value);
            }
        };
    }

    private static StructurePoolConfig defaults() {
        var config = new StructurePoolConfig();
        var largeWeight = 0;
        var smallWeight = 5;
        var limit = 1;
        config.entries = new ArrayList<>(List.of(
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
        return config;
    }
}
