package net.archers.client;

import net.rpg_foundation.armor_api.client.ArmorRenderers;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.archers.ArchersMod;
import net.archers.client.armor.ArcherArmorRenderer;
import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.effect.ArcherEffects;
import net.archers.item.ArcherArmors;
import net.archers.item.Quivers;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.rpg_series.item.Armor;

import java.util.List;

public class ArchersClientMod {
    public static void init() {
        CustomModelStatusEffect.register(ArcherEffects.HUNTERS_MARK.effect, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(ArcherEffects.ENTANGLING_ROOTS.effect, new RootsRenderer());

        // Archers' custom tooltip lines (ArchersTooltip.addLines) are wired per-platform from each client
        // entrypoint's native tooltip event (Fabric ItemTooltipCallback / NeoForge ItemTooltipEvent).

        registerArmorRenderer(ArcherArmors.archerArmorSet_T1, ArcherArmorRenderer.archer());
        registerArmorRenderer(ArcherArmors.archerArmorSet_T2, ArcherArmorRenderer.ranger());
        registerArmorRenderer(ArcherArmors.archerArmorSet_T3, ArcherArmorRenderer.netheriteRanger());

        List<Identifier> quiverModels = Quivers.entries.stream()
                .map(entry -> new Identifier(ArchersMod.ID, "item/quiver/" + entry.id().getPath()))
                .toList();
        CustomModels.registerModelIds(quiverModels);
    }

    private static void registerArmorRenderer(Armor.Set set, GeoArmorRenderer renderer) {
        ArmorRenderers.register(renderer, set.head, set.chest, set.legs, set.feet);
    }
}
