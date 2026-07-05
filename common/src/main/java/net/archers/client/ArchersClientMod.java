package net.archers.client;

import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.archers.ArchersMod;
import net.archers.client.armor.ArcherArmorRenderer;
import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.client.util.ArchersTooltip;
import net.archers.effect.ArcherEffects;
import net.archers.item.ArcherArmors;
import net.archers.item.Quivers;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.rpg_series.item.Armor;

import java.util.List;
import java.util.function.Supplier;

public class ArchersClientMod {
    public static void init() {
        CustomModelStatusEffect.register(ArcherEffects.HUNTERS_MARK.effect, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(ArcherEffects.ENTANGLING_ROOTS.effect, new RootsRenderer());

        ArchersTooltip.init();

        SpellTooltip.addDescriptionMutator(Identifier.of(ArchersMod.ID, "power_shot"), (args) -> {
            var description = args.description();
            var huntersMarkPercent = SpellTooltip.percent(ArcherEffects.HUNTERS_MARK.config().firstModifier().value);
            description = description.replace(SpellTooltip.placeholder("damage_taken"), huntersMarkPercent);
            return description;
        });

        registerArmorRenderer(ArcherArmors.archerArmorSet_T1, ArcherArmorRenderer::archer);
        registerArmorRenderer(ArcherArmors.archerArmorSet_T2, ArcherArmorRenderer::ranger);
        registerArmorRenderer(ArcherArmors.archerArmorSet_T3, ArcherArmorRenderer::netheriteRanger);

        List<Identifier> quiverModels = Quivers.entries.stream()
                .map(entry -> Identifier.of(ArchersMod.ID, "item/quiver/" + entry.id().getPath()))
                .toList();
        CustomModels.registerModelIds(quiverModels);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
}
