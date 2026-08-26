package net.archers.effect;

import net.archers.ArchersMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.CustomStatusEffect;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.entity.SpellEngineAttributes;

import java.util.ArrayList;
import java.util.List;

public class ArcherEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Effects.Entry HUNTERS_MARK_STASH = add(new Effects.Entry(
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "hunters_mark_stash"),
            "Power Shot",
            "Next shot applies Hunter's Mark",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0xff0000)
    ));

    public static Effects.Entry HUNTERS_MARK = add(new Effects.Entry(
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "hunters_mark"),
            "Marked",
            "Increases damage taken from all sources",
            new CustomStatusEffect(MobEffectCategory.HARMFUL, 0xff0000),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                            0.1F,
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            ))
    ));

    public static final Effects.Entry ENTANGLING_ROOTS = add(new Effects.Entry(
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "entangling_roots"),
            "Entangling Roots",
            "Reduces movement speed",
            new CustomStatusEffect(MobEffectCategory.HARMFUL, 0x993333),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            Attributes.MOVEMENT_SPEED.getRegisteredName(),
                            -0.5F,
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new AttributeModifier(
                            Attributes.JUMP_STRENGTH.getRegisteredName(),
                            -0.5F,
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            ))
    ));

    public static void register(ConfigFile.Effects config) {
        Synchronized.configure(HUNTERS_MARK.effect, true);
        Synchronized.configure(ENTANGLING_ROOTS.effect, true);

        Effects.register(entries, config.effects);
    }
}
