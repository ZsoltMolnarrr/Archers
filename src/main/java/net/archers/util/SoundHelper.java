package net.archers.util;

import net.archers.ArchersMod;
import net.archers.block.ArcherWorkbenchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class SoundHelper {
    public static List<String> soundKeys = List.of(
        "entangling_roots",
        "bow_pull",
        "magic_arrow_impact",
        "magic_arrow_release",
        "magic_arrow_start"
    );

    public static Map<String, Float> soundDistances = Map.of(
        // "sound_name", Float.valueOf(48F)
    );

    public static final SoundEvent WORKBENCH = SoundEvent.of(ArcherWorkbenchBlock.ID);

    public static void registerSounds() {
        for (var soundKey: soundKeys) {
            var soundId = new Identifier(ArchersMod.ID, soundKey);
            var customTravelDistance = soundDistances.get(soundKey);
            var soundEvent = (customTravelDistance == null)
                    ? SoundEvent.of(soundId)
                    : SoundEvent.of(soundId, customTravelDistance);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }

        Registry.register(Registries.SOUND_EVENT, ArcherWorkbenchBlock.ID, WORKBENCH);
    }

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