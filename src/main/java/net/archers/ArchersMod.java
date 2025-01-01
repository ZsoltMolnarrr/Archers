package net.archers;

import net.archers.block.ArcherBlocks;
import net.archers.config.ArchersItemConfig;
import net.archers.config.Default;
import net.archers.config.TweaksConfig;
import net.archers.effect.Effects;
import net.archers.item.Group;
import net.archers.item.Weapons;
import net.archers.item.Armors;
import net.archers.item.misc.Misc;
import net.archers.util.SoundHelper;
import net.archers.village.ArcherVillagers;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.spell.SpellContainer;
import net.tinyconfig.ConfigManager;

public class ArchersMod implements ModInitializer {
    public static final String ID = "archers";

    public static ConfigManager<ArchersItemConfig> itemConfig = new ConfigManager<ArchersItemConfig>
            ("items_v4", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StructurePoolConfig> villagesConfig = new ConfigManager<>
            ("villages", Default.villages)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
            ("tweaks", new TweaksConfig())
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        tweaksConfig.refresh();
        registerItemGroup();
        registerItems();
        SoundHelper.registerSounds();
        Effects.register();
        registerVillages();
        subscribeEvents();

        // Apply some of the tweaks
        if (tweaksConfig.value.enable_infinity_for_crossbows) {
            EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> {
                if (target.getItem() instanceof CrossbowItem &&
                        enchantment.getKey().get().getValue().equals(Enchantments.INFINITY.getValue())) {
                    return TriState.TRUE;
                }
                return TriState.DEFAULT;
            });
        }
//        CrossbowMechanics.PullTime.modifier = (originalPullTime, crossbow) -> {
//            int quickCharge = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, crossbow);
//            var multiplier = tweaksConfig.value.quick_charge_enchantment_multiplier_per_level;
//            return originalPullTime - (int)((double)originalPullTime * multiplier) * quickCharge;
//        };
    }

    private void registerItemGroup() {
        Group.ARCHERS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.archerArmorSet_T2.head.asItem()))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.ARCHERS);
    }

    private void registerItems() {
        itemConfig.refresh();
        SpellBooks.createAndRegister(Identifier.of(ID, "archer"), SpellContainer.ContentType.ARCHERY, Group.KEY);
        ArcherBlocks.register();
        Misc.register();
        Weapons.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
    }

    private void registerVillages() {
        villagesConfig.refresh();
        ArcherVillagers.register();
    }

    private void subscribeEvents() {
    }
}