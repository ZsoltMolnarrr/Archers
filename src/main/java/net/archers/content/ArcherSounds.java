package net.archers.content;

import net.archers.ArchersMod;
import net.archers.block.ArcherWorkbenchBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ArcherSounds {
    public static class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;
        private int variants = 1;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(Identifier.of(ArchersMod.ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Entry travelDistance(float distance) {
            return new Entry(id, SoundEvent.of(id, distance));
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        public Identifier id() {
            return id;
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public RegistryEntry<SoundEvent> entry() {
            return entry;
        }

        public int variants() {
            return variants;
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry MARKER_SHOT = add(new Entry("marker_shot"));
    public static final Entry ENTANGLING_ROOTS = add(new Entry("entangling_roots"));
    public static final Entry BOW_PULL = add(new Entry("bow_pull"));
    public static final Entry MAGIC_ARROW_IMPACT = add(new Entry("magic_arrow_impact"));
    public static final Entry MAGIC_ARROW_RELEASE = add(new Entry("magic_arrow_release"));
    public static final Entry MAGIC_ARROW_START = add(new Entry("magic_arrow_start"));
    public static final Entry WORKBENCH = add(new Entry(ArcherWorkbenchBlock.ID.getPath()));
    public static final Entry ARCHER_ARMOR_EQUIP = add(new Entry("archer_armor"));

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }
}