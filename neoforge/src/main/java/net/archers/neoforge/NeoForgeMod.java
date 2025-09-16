package net.archers.neoforge;

import net.archers.ArchersMod;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ArchersMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        ArchersMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            ArchersMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            ArchersMod.registerItems();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            ArchersMod.registerBlocks();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            ArchersMod.registerEffects();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            // Not sure why errors are thrown, but this seems to fix it.
            try {
                ArchersMod.registerPOI();
            } catch (Exception e) { }
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            ArchersMod.registerVillagers();
        });
    }
}
