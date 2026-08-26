package net.archers.block;

import net.archers.ArchersMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import java.util.ArrayList;

public class ArcherBlocks {

    public record Entry(String name, Block block, BlockItem item) {
        public Entry(String name, Block block) {
            // Since 1.21.2 every `Item.Settings` must carry its registry key (the item crashes at
            // construction otherwise), and a block item needs `useBlockPrefixedTranslationKey()` to keep
            // its `block.<ns>.<path>` lang key.
            this(name, block, new BlockItem(block, new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ArchersMod.ID, name)))
                    .useBlockDescriptionPrefix()));
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Block block) {
        var entry = new Entry(name, block);
        all.add(entry);
        return entry;
    }

    public static final Entry WORKBENCH = entry(ArcherWorkbenchBlock.ID.getPath(), new ArcherWorkbenchBlock(
            BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ArcherWorkbenchBlock.ID))
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
    ));

    public static void register() {
        for (var entry : all) {
            Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(ArchersMod.ID, entry.name), entry.block);
            Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ArchersMod.ID, entry.name), entry.item());
        }
        // Creative-tab placement (into the Archers group) is registered per-platform from each loader's
        // entrypoint, iterating ArcherBlocks.all — no Fabric API ItemGroupEvents in common.
    }
}
