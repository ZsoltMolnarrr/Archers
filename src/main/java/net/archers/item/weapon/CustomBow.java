package net.archers.item.weapon;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CustomBow extends BowItem {
    // Instances are kept a list of, so model predicates can be automatically registered
    public final static ArrayList<CustomBow> instances = new ArrayList<>();
    public CustomBow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings);
        this.repairIngredientSupplier = repairIngredientSupplier;
        instances.add(this);
    }

    private final Supplier<Ingredient> repairIngredientSupplier;

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.repairIngredientSupplier.get().test(ingredient) || super.canRepair(stack, ingredient);
    }
}
