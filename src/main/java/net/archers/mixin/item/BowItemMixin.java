package net.archers.mixin.item;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.ArchersMod;
import net.archers.item.weapon.CustomRangedWeaponProperties;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin implements CustomRangedWeaponProperties {
    private int customPullTime = 0;
    @Override
    public int getCustomPullTime_RPGS() {
        return customPullTime;
    }
    @Override
    public void setCustomPullTime_RPGS(int pullTime) {
        customPullTime = pullTime;
    }

    private float customVelocity = 0;
    @Override
    public float getCustomVelocity_RPGS() {
        return customVelocity;
    }
    @Override
    public void setCustomVelocity_RPGS(float velocity) {
        customVelocity = velocity;
    }

    public float getCustomPullProgress(int useTicks) {
        float pullTime = this.customPullTime > 0 ? this.customPullTime : 20;
        float f = (float)useTicks / pullTime;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }


    /**
     * Apply custom pull time
     */
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;getPullProgress(I)F"))
    private float applyCustomPullTime(int ticks, Operation<Float> original) {
        if (customPullTime > 0) {
            return getCustomPullProgress(ticks);
        } else {
            return original.call(ticks);
        }
    }

    /**
     * Apply custom velocity
     */
    @WrapWithCondition(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean applyCustomVelocity(World world, Entity entity) {
        if (entity instanceof PersistentProjectileEntity projectile) {
            if (customVelocity > 0F) {
                // 3.0F is the default hardcoded velocity of bows
                projectile.setVelocity(projectile.getVelocity().multiply(customVelocity / 3.0F));
            }
        }
        return true;
    }

    /**
     * Apply power enchantment multiplier
     */
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setDamage(D)V"))
    private void applyPowerEnchantmentMultiplier(
            // Mixin Parameters
            PersistentProjectileEntity projectile, double damage, Operation<Void> original,
            // Context Parameters
            ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        var configValue = ArchersMod.enchantmentsConfig.value.power_multiplier_per_level;
        if (configValue > 0) {
            // Replacing Power enchantment bonus due to poorly scaling
            int level = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (level > 0) {
                projectile.setDamage(projectile.getDamage() * (1 + level * configValue));
            }
        } else {
            original.call();
        }
    }
}
