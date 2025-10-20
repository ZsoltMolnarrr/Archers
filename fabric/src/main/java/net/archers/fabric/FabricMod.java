package net.archers.fabric;

import net.archers.ArchersMod;
import net.archers.fabric.client.trinkets.QuiverRenderer;
import net.archers.item.Quivers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.client.render.CustomModelRegistry;

import java.util.List;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        ArchersMod.init();
        ArchersMod.registerSounds();
        ArchersMod.registerBlocks();
        ArchersMod.registerItems();
        ArchersMod.registerEffects();
        ArchersMod.registerPOI();
        ArchersMod.registerVillagers();

        List<Identifier> quiverModels = Quivers.entries.stream()
                .map(entry -> Identifier.of(ArchersMod.ID, "quiver/" + entry.id().getPath()))
                .toList();
        CustomModels.registerModelIds(quiverModels);
    }
}
