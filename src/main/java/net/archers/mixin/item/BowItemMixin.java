package net.archers.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.ArchersMod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {
    /**
     * Modify damage bonus of vanilla Power enchantment
     */
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setDamage(D)V"))
    private void applyPowerEnchantmentMultiplier(
            // Mixin Parameters
            PersistentProjectileEntity projectile, double damage, Operation<Void> original,
            // Context Parameters
            ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        var configValue = ArchersMod.tweaksConfig.value.power_enchantment_multiplier_per_level;
        if (configValue > 0) {
            int level = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (level > 0) {
                projectile.setDamage(projectile.getDamage() * (1 + level * configValue));
            }
        } else {
            original.call();
        }
    }
}
