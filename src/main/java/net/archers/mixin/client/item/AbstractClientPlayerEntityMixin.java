package net.archers.mixin.client.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.archers.item.weapon.CustomBow;
import net.archers.item.weapon.CustomRangedWeaponProperties;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @WrapOperation(
            method = "getFovMultiplier",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    )
    private boolean getFovMultiplier_CustomBows(ItemStack itemStack, Item item, Operation<Boolean> original) {
        if (item == Items.BOW) {
            if (CustomBow.instances.contains(itemStack.getItem())) {
                return true;
            }
        }
        return original.call(itemStack, item);
    }

    @ModifyConstant(method = "getFovMultiplier", constant = @Constant(floatValue = 20.0F))
    private float getFovMultiplier_CustomBows_PullTime(float value) {
        var item = ((AbstractClientPlayerEntity)(Object)this).getActiveItem().getItem();
        if (CustomBow.instances.contains(item)) {
            // Override hardcoded pull time
            return ((CustomRangedWeaponProperties)item).getCustomPullTime_RPGS();
        } else {
            return value;
        }
    }
}
