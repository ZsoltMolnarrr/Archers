package net.archers.item;

import com.github.theredbrain.bundleapi.BundleAPI;
import com.github.theredbrain.bundleapi.component.type.CustomBundleContentsComponent;
import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import net.archers.ArchersMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Quivers {
    public static final List<Entry> entries = new ArrayList<>();
    public record Entry(Identifier id, int capacity, Item item) {  }
    public record Args(TagKey<Item> tag, Component emptyDescription, Item.Properties settings) { }
    public static Function<Args, Item> factory = args -> new CustomBundleItem(args.tag, args.emptyDescription, args.settings);
    public static Entry entry(String name, int capacity, @Nullable Rarity rarity) {
        var id = Identifier.fromNamespaceAndPath(ArchersMod.ID, name);
        // Since 1.21.2 every `Item.Settings` must carry its registry key or the item crashes at construction.
        var settings = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .stacksTo(1)
                .component(
                        DataComponents.LORE,
                        new ItemLore(List.of(
                                Component.translatable("item.archers.quiver.hint")
                                        .withStyle(ChatFormatting.GRAY)
                        ))
                )
                .component(
                        BundleAPI.CUSTOM_BUNDLE_CONTENTS_COMPONENT,
                        CustomBundleContentsComponent.builder().size_multiplier(capacity).build()
                );
        if (rarity != null) {
            settings.rarity(rarity);
        }
        var bundle = factory.apply(new Args(ItemTags.ARROWS, Component.translatable("item.archers.quiver.empty.description"), settings));
        var entry = new Entry(id, capacity, bundle);
        entries.add(entry);
        return entry;
    }

    public static void register() {
        entry("small_quiver", 4, null);
        entry("medium_quiver", 8, null);
        entry("large_quiver", 12, Rarity.UNCOMMON);

        for(var entry: entries) {
            Registry.register(BuiltInRegistries.ITEM, entry.id(), entry.item());
        }
        // Creative-tab placement (COMBAT/Archers group) is registered per-platform from each loader's
        // entrypoint, iterating Quivers.entries (guarded by the bundleapi check) — no Fabric API here.
    }
}
