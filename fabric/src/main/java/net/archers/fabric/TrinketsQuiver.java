package net.archers.fabric;

import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

public class TrinketsQuiver extends CustomBundleItem implements Trinket {
    public TrinketsQuiver(@Nullable TagKey<Item> tag, Settings settings) {
        super(tag, settings);
    }

    public static void render() {
        // TrinketRendererRegistry.registerRenderer();
    }
}
