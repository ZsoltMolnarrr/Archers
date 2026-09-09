package net.archers.block;

import net.archers.ArchersMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ArcherBlocks {

    public record Entry(String name, Block block, BlockItem item) {
        public Entry(String name, Block block) {
            this(name, block, new BlockItem(block, new Item.Settings()));
        }

        /// The id both the block and its block item register under. Shared so the Forge entrypoint's
        /// registration loop cannot drift from the one below.
        public Identifier id() {
            return new Identifier(ArchersMod.ID, name);
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
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(Instrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
    ));

    /// Split from {@link #registerItems()} for Forge 47: `RegisterEvent` opens one window per registry and
    /// locks every other one, so blocks and block items cannot be registered from the same window.
    public static void registerBlocks() {
        for (var entry : all) {
            Registry.register(Registries.BLOCK, entry.id(), entry.block);
        }
    }

    public static void registerItems() {
        for (var entry : all) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        // Creative-tab placement (into the Archers group) is loader-neutral, dispatched by SpellEngine's
        // `PlatformEvents.onItemGroupModify` from `ArchersMod.registerItems()` — registered there ahead of
        // the weapon/armor registrations so the blocks come first in the tab.
    }
}
