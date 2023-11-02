package net.archers.effect;

import net.archers.ArchersMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.HealthImpacting;
import net.spell_engine.api.effect.Synchronized;

public class Effects {
    public static final StatusEffect huntersMark = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff0000);
    public static final StatusEffect entanglingRoots = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x993333)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "112f3133-8a44-11ed-a1eb-0242ac120002",
                        -0.5F,
                        EntityAttributeModifier.Operation.MULTIPLY_BASE);


    public static void register() {
        Synchronized.configure(huntersMark, true);
        var modifierPerStack = 0.05F; // When changing this value, make sure to update the value in language files too
        HealthImpacting.configureDamageTaken(huntersMark, modifierPerStack);

        Synchronized.configure(entanglingRoots, true);

        int rawId = 743;
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(ArchersMod.ID, "hunters_mark").toString(), huntersMark);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(ArchersMod.ID, "entangling_roots").toString(), entanglingRoots);
    }
}
