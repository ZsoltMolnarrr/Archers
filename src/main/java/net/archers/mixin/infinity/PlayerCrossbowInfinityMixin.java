package net.archers.mixin.infinity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.ArchersMod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerCrossbowInfinityMixin {

    @WrapOperation(method = "getProjectileType",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"))
    private static boolean wrap_loadProjectiles(
            // Mixin Parameters
            PlayerAbilities abilities, Operation<Boolean> original,
            // Context Parameters
            ItemStack stack
    ) {
        if (ArchersMod.tweaksConfig.value.enable_infinity_for_crossbows
                && stack.getItem() instanceof CrossbowItem
                && EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0) {
            return true;
        } else {
            return original.call(abilities);
        }
    }

}
