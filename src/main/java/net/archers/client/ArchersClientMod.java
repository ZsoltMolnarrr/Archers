package net.archers.client;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererRegistry;
import net.archers.ArchersMod;
import net.archers.block.ArcherBlocks;
import net.archers.client.armor.ArcherArmorRenderer;
import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.effect.Effects;
import net.archers.item.Armors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.client.gui.SpellTooltip;

import java.util.List;
import java.util.function.Supplier;

public class ArchersClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ArcherBlocks.WORKBENCH.block(), RenderLayer.getCutout());

        CustomModels.registerModelIds(List.of(
                HuntersMarkRenderer.modelId,
                RootsRenderer.modelId,
                Identifier.of(ArchersMod.ID, "projectile/magic_arrow")
        ));
        CustomModelStatusEffect.register(Effects.HUNTERS_MARK.effect, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(Effects.ENTANGLING_ROOTS.effect, new RootsRenderer());

        ArchersTooltip.init();

        var config = ArchersMod.tweaksConfig.value;
        SpellTooltip.addDescriptionMutator(Identifier.of(ArchersMod.ID, "power_shot"), (args) -> {
            var description = args.description();
            var huntersMarkPercent = ((int)(config.hunters_mark_damage_per_stack * 100)) + "%";
            description = description.replace(SpellTooltip.placeholder("damage_taken"), "" + huntersMarkPercent);
            return description;
        });

        registerArmorRenderer(Armors.archerArmorSet_T1, ArcherArmorRenderer::archer);
        registerArmorRenderer(Armors.archerArmorSet_T2, ArcherArmorRenderer::ranger);
        registerArmorRenderer(Armors.archerArmorSet_T3, ArcherArmorRenderer::netheriteRanger);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
}
