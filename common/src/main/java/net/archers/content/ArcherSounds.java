package net.archers.content;

import net.archers.ArchersMod;
import net.archers.block.ArcherWorkbenchBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            this(new Identifier(ArchersMod.ID, name));
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
    public static final Entry RAIN_OF_ARROWS_RELEASE = add(new Entry("rain_of_arrows_release"));
    public static final Entry RAIN_OF_ARROWS_IMPACT = add(new Entry("rain_of_arrows_impact"));
    public static final Entry SPIRIT_WOLF_SPAWN = add(new Entry("spirit_wolf_spawn"));
    public static final Entry SPIRIT_WOLF_SUMMON = add(new Entry("spirit_wolf_summon").variants(2));



    /// Creation only — the sound events keyed by the id they register under. A loader that registers
    /// them itself (Forge) iterates this instead of calling {@link #register()}.
    ///
    /// The `Entry#entry` `RegistryEntry` is deliberately not reproduced on that path: Forge's
    /// `RegisterEvent` helper returns void, and nothing ever reads `entry()` at runtime — armor materials
    /// take the raw `SoundEvent` and spells reference sounds by id.
    public static Map<Identifier, SoundEvent> soundsToRegister() {
        var sounds = new LinkedHashMap<Identifier, SoundEvent>();
        for (var entry: entries) {
            sounds.put(entry.id(), entry.soundEvent());
        }
        return sounds;
    }

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }
}