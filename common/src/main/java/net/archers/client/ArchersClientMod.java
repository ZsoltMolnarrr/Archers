package net.archers.client;

import net.rpg_foundation.armor_api.client.ArmorRenderers;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.archers.client.armor.ArcherArmorRenderer;
import net.archers.client.compatibility.IrisCompat;
import net.archers.client.effect.HuntersMarkRenderer;
import net.archers.client.effect.RootsRenderer;
import net.archers.effect.ArcherEffects;
import net.archers.item.ArcherArmors;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.rpg_series.item.Armor;

public class ArchersClientMod {
    public static void init() {
        // Declare our own render pipelines to Iris - without this the spirit layer is composited away
        // under every shader pack (see IrisCompat).
        IrisCompat.assignPipelines();

        CustomModelStatusEffect.register(ArcherEffects.HUNTERS_MARK.effect, new HuntersMarkRenderer());
        CustomModelStatusEffect.register(ArcherEffects.ENTANGLING_ROOTS.effect, new RootsRenderer());

        // Archers' custom tooltip lines (ArchersTooltip.addLines) are wired per-platform from each client
        // entrypoint's native tooltip event (Fabric ItemTooltipCallback / NeoForge ItemTooltipEvent).

        registerArmorRenderer(ArcherArmors.archerArmorSet_T1, ArcherArmorRenderer.archer());
        registerArmorRenderer(ArcherArmors.archerArmorSet_T2, ArcherArmorRenderer.ranger());
        registerArmorRenderer(ArcherArmors.archerArmorSet_T3, ArcherArmorRenderer.netheriteRanger());

        // Worn quivers render through the vanilla item-model path (see WornQuiverRenderer), so the 3D
        // `item/quiver/*` models are loaded by the item-model definitions — no extra model registration.
    }

    private static void registerArmorRenderer(Armor.Set set, GeoArmorRenderer renderer) {
        ArmorRenderers.register(renderer, set.head, set.chest, set.legs, set.feet);
    }
}
