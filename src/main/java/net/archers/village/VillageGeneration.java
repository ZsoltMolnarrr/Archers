package net.archers.village;

import com.mojang.datafixers.util.Pair;
import net.archers.ArchersMod;
import net.archers.mixin.StructurePoolAccessor;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class VillageGeneration {
    public static void init(MinecraftServer server) {
        var config = ArchersMod.worldGenConfig.value;
        for(var entry: config.entries) {
            // System.out.println("Adding structure: " + entry.getValue() + " to: " + entry.getKey());
            for (var structure: entry.structures) {
                addToStructurePool(server, new Identifier(entry.pool), new Identifier(structure.id), structure.weight);
            }
        }
    }

    private static final RegistryKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = RegistryKey.of(
            RegistryKeys.PROCESSOR_LIST, new Identifier("minecraft", "empty"));


    private static void addToStructurePool(MinecraftServer server, Identifier poolId, Identifier structureId, int weight) {
        RegistryEntry<StructureProcessorList> emptyProcessorList = server.getRegistryManager()
                .get(RegistryKeys.PROCESSOR_LIST)
                .entryOf(EMPTY_PROCESSOR_LIST_KEY);

        var poolGetter = server.getRegistryManager()
                .get(RegistryKeys.TEMPLATE_POOL)
                .getOrEmpty(poolId);

        if (poolGetter.isEmpty()) {
            System.err.println(ArchersMod.ID + ": cannot add to " + poolId + " as it cannot be found!");
            return;
        }

        var pool = poolGetter.get();

        var pieceList = ((StructurePoolAccessor) pool).getElements();
        var piece = StructurePoolElement.ofProcessedSingle(structureId.toString(), emptyProcessorList)
                .apply(StructurePool.Projection.RIGID);

        var list = new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());
        list.add(Pair.of(piece, weight));
        ((StructurePoolAccessor) pool).setElementCounts(list);

        for (int i = 0; i < weight; ++i) {
            pieceList.add(piece);
        }
    }
}
