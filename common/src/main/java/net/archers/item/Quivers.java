package net.archers.item;

import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import net.archers.ArchersMod;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Quivers {
    public static final List<Entry> entries = new ArrayList<>();
    public record Entry(Identifier id, int capacity, Item item) {  }

    /// 1.20.1 / BundleAPI 1.1: there are no data components, so the capacity multiplier is a constructor
    /// argument on the item (it used to ride in `Item.Settings#component(CUSTOM_BUNDLE_CONTENTS, …)`).
    public record Args(TagKey<Item> tag, int capacity, Item.Settings settings) { }
    public static Function<Args, Item> factory = args -> new QuiverItem(args.tag, args.capacity, args.settings);

    /// A quiver: a whitelist bundle that also carries the "provides arrows when equipped" hint line.
    /// 1.20.1 has no `LoreComponent`, so the hint is an `appendTooltip` override instead of item defaults.
    public static class QuiverItem extends CustomBundleItem {
        public QuiverItem(@Nullable TagKey<Item> tag, int sizeMultiplier, Settings settings) {
            super(tag, sizeMultiplier, settings);
        }

        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            super.appendTooltip(stack, world, tooltip, context);
            tooltip.add(Text.translatable("item.archers.quiver.hint").formatted(Formatting.GRAY));
        }
    }

    public static Entry entry(String name, int capacity, @Nullable Rarity rarity) {
        var settings = new Item.Settings().maxCount(1);
        if (rarity != null) {
            settings.rarity(rarity);
        }
        var bundle = factory.apply(new Args(ItemTags.ARROWS, capacity, settings));
        var id = new Identifier(ArchersMod.ID, name);
        var entry = new Entry(id, capacity, bundle);
        entries.add(entry);
        return entry;
    }

    public static void register() {
        entry("small_quiver", 4, null);
        entry("medium_quiver", 8, null);
        entry("large_quiver", 12, Rarity.UNCOMMON);

        for(var entry: entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        // Creative-tab placement (COMBAT/Archers group) is registered per-platform from each loader's
        // entrypoint, iterating Quivers.entries (guarded by the bundleapi check) — no Fabric API here.
    }
}
