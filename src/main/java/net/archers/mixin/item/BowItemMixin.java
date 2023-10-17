package net.archers.mixin.item;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.item.CustomBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;getPullProgress(I)F"))
    private float applyCustomPullTime(int ticks, Operation<Float> original) {
        Object bow = this;
        if (bow instanceof CustomBow customBow) {
            return customBow.getCustomPullProgress(ticks);
        } else {
            return original.call(ticks);
        }
    }

    @WrapWithCondition(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean applyCustomVelocity(World world, Entity entity) {
        Object bow = this;
        var projectile = (PersistentProjectileEntity)entity;
        if (bow instanceof CustomBow customBow) {
            if (customBow.customVelocity > 0F) {
                // 3.0F is the default hardcoded velocity of bows
                projectile.setVelocity(projectile.getVelocity().multiply(customBow.customVelocity / 3.0F));
            }
        }
        return true;
    }
}
