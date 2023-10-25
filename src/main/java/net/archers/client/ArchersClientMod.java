package net.archers.client;

import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.client.util.ModelPredicateHelper;
import net.archers.effect.Effects;
import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomCrossbow;
import net.fabricmc.api.ClientModInitializer;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

public class ArchersClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var bow: CustomBow.instances) {
            ModelPredicateHelper.registerBowModelPredicates(bow);
        }
        for (var crossbow: CustomCrossbow.instances) {
            ModelPredicateHelper.registerCrossbowModelPredicates(crossbow);
        }
        ArchersTooltip.init();

        CustomModels.registerModelIds(List.of(
                HuntersMarkRenderer.modelId,
                RootsRenderer.modelId
        ));
        CustomModelStatusEffect.register(Effects.huntersMark, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(Effects.entanglingRoots, new RootsRenderer());
    }
}
