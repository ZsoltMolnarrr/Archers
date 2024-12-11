package net.archers.util;

import net.archers.ArchersMod;
import net.archers.block.ArcherWorkbenchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class SoundHelper {
    public static List<String> soundKeys = List.of(
        "marker_shot",
        "entangling_roots",
        "bow_pull",
        "magic_arrow_impact",
        "magic_arrow_release",
        "magic_arrow_start"
    );

    public static Map<String, Float> soundDistances = Map.of(
        // "sound_name", Float.valueOf(48F)
    );

    public static void registerSounds() {
        for (var soundKey: soundKeys) {
            var soundId = Identifier.of(ArchersMod.ID, soundKey);
            var customTravelDistance = soundDistances.get(soundKey);
            var soundEvent = (customTravelDistance == null)
                    ? SoundEvent.of(soundId)
                    : SoundEvent.of(soundId, customTravelDistance);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }
    }

    public record Entry(Identifier id, SoundEvent sound, RegistryEntry<SoundEvent> entry) {}

    private static Entry registerSound(String key) {
        var soundId = Identifier.of(ArchersMod.ID, key);
        var event = SoundEvent.of(soundId);
        var entry = Registry.registerReference(Registries.SOUND_EVENT, soundId, event);
        return new Entry(soundId, event, entry);
    }

    public static final Entry WORKBENCH = registerSound(ArcherWorkbenchBlock.ID.getPath());
    public static final Entry ARCHER_ARMOR_EQUIP = registerSound("cloth_equip");

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent) {
        playSoundEvent(world, entity, soundEvent, 1, 1);
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent, float volume, float pitch) {
        world.playSound(
                (PlayerEntity)null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                soundEvent,
                SoundCategory.PLAYERS,
                volume,
                pitch);
    }
}