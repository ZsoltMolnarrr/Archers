package net.archers.client;

import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.effect.Effects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

public class ArchersClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ArcherBlocks.WORKBENCH.block(), RenderLayer.getCutout());

        CustomModels.registerModelIds(List.of(
                HuntersMarkRenderer.modelId,
                RootsRenderer.modelId,
                new Identifier(ArchersMod.ID, "projectile/magic_arrow")
        ));
        CustomModelStatusEffect.register(Effects.huntersMark, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(Effects.entanglingRoots, new RootsRenderer());
    }
}
