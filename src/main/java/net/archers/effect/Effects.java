package net.archers.effect;

import net.archers.ArchersMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.HealthImpacting;
import net.spell_engine.api.effect.SpellStash;
import net.spell_engine.api.effect.Synchronized;

import java.util.ArrayList;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(ArchersMod.ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(ArchersMod.ID, "effect." + id.getPath());
        }
    }

    public static final Entry MARKER_SHOT = new Entry("marker_shot",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff0000));
    public static final Entry HUNTERS_MARK = new Entry("hunters_mark", 
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff0000));
    public static final Entry ENTANGLING_ROOTS = new Entry("entangling_roots",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x993333));


    public static void register() {
        var config = ArchersMod.tweaksConfig.value;

        ENTANGLING_ROOTS.effect.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                ENTANGLING_ROOTS.modifierId(),
                -0.5F,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SpellStash.configure(MARKER_SHOT.effect, Identifier.of(ArchersMod.ID, "power_shot"), 1);
        Synchronized.configure(HUNTERS_MARK.effect, true);
        HealthImpacting.configureDamageTaken(HUNTERS_MARK.effect, config.hunters_mark_damage_per_stack);
        Synchronized.configure(ENTANGLING_ROOTS.effect, true);

        for (var entry: entries) {
            entry.register();
        }
    }
}
