package net.archers.mixin.infinity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.ArchersMod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowInfinityMixin {

    @WrapOperation(method = "loadProjectiles",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"))
    private static boolean wrap_loadProjectiles(
            // Mixin Parameters
            PlayerAbilities abilities, Operation<Boolean> original,
            // Context Parameters
            LivingEntity shooter, ItemStack crossbow
    ) {
        if (ArchersMod.tweaksConfig.value.enable_infinity_for_crossbows
                && EnchantmentHelper.getLevel(Enchantments.INFINITY, crossbow) > 0) {
            return true;
        } else {
            return original.call(abilities);
        }
    }

    @WrapOperation(method = "loadProjectiles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;loadProjectile(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;ZZ)Z"))
    private static boolean wrap_loadProjectiles(
            // Mixin Parameters
            LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative, Operation<Boolean> original
    ) {
        var infinity = ArchersMod.tweaksConfig.value.enable_infinity_for_crossbows
                && EnchantmentHelper.getLevel(Enchantments.INFINITY, crossbow) > 0;
        if (infinity && !projectile.isOf(Items.ARROW)) {
            // System.out.println("Infinity crossbow loaded with non-arrow projectile: " + projectile);
            return original.call(shooter, crossbow, projectile, simulated, false);
        } else {
            return original.call(shooter, crossbow, projectile, simulated, creative);
        }
    }

    @WrapOperation(method = "shootAll",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"))
    private static boolean wrap_isCreativeMode(
            // Mixin Parameters
            PlayerAbilities abilities, Operation<Boolean> original,
            // Context Parameters
            World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence
    ) {
        if (ArchersMod.tweaksConfig.value.enable_infinity_for_crossbows
                && EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0) {
            return true;
        } else {
            return original.call(abilities);
        }
    }
}
