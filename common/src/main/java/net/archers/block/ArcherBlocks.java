package net.archers.block;

import net.archers.ArchersMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ArcherBlocks {

    public record Entry(String name, Block block, BlockItem item) {
        public Entry(String name, Block block) {
            // Since 1.21.2 every `Item.Settings` must carry its registry key (the item crashes at
            // construction otherwise), and a block item needs `useBlockPrefixedTranslationKey()` to keep
            // its `block.<ns>.<path>` lang key.
            this(name, block, new BlockItem(block, new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ArchersMod.ID, name)))
                    .useBlockPrefixedTranslationKey()));
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Block block) {
        var entry = new Entry(name, block);
        all.add(entry);
        return entry;
    }

    public static final Entry WORKBENCH = entry(ArcherWorkbenchBlock.ID.getPath(), new ArcherWorkbenchBlock(
            AbstractBlock.Settings.create()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, ArcherWorkbenchBlock.ID))
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
    ));

    public static void register() {
        for (var entry : all) {
            Registry.register(Registries.BLOCK, Identifier.of(ArchersMod.ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, Identifier.of(ArchersMod.ID, entry.name), entry.item());
        }
        // Creative-tab placement (into the Archers group) is registered per-platform from each loader's
        // entrypoint, iterating ArcherBlocks.all — no Fabric API ItemGroupEvents in common.
    }
}
