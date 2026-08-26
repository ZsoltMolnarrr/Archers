package net.archers.mixin.client.autofire;

import net.archers.client.util.ItemUseDelay;
import net.archers.item.misc.AutoFireHook;
import net.fabric_extras.ranged_weapon.api.RangedWeaponProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    /// `ModelPredicateProviderRegistry` (and the `pull` model predicate it served) was removed with the
    /// 1.21.4 item-model overhaul, and Ranged Weapon API's stop-gap `ranged_weapon:pull` numeric item
    /// model property is deprecated in favour of the vanilla ones. The charge progress is therefore
    /// computed here from Ranged Weapon API's stable data API, rather than by evaluating (or looking up
    /// by id) an item model property — the item-model definition of a third-party bow may not use it at all.
    ///
    /// Note `UseDurationProperty.getTicksUsedSoFar` is the raw tick count: unlike
    /// `UseDurationProperty.getValue`, it is not rescaled by Ranged Weapon API to vanilla's 20-tick pull.
    private static float archers$pullProgress(ItemStack stack, LivingEntity entity) {
        if (entity.getActiveItem() != stack) {
            return 0.0F;
        }
        if (stack.getItem() instanceof CrossbowItem) {
            if (CrossbowItem.isCharged(stack)) {
                return 0.0F;
            }
            return (float) UseDurationProperty.getTicksUsedSoFar(stack, entity) / CrossbowItem.getPullTime(stack, entity);
        }
        var pullTime = RangedWeaponProperties.pullTimeTicks(stack, 20);
        if (pullTime <= 0) {
            return 0.0F;
        }
        return (float) UseDurationProperty.getTicksUsedSoFar(stack, entity) / pullTime;
    }

    private int timeHoldingCharged = 0;
    private boolean autoFiring = false;
    @Inject(method = "tickItemStackUsage", at = @At("HEAD"))
    private void autoFireHookRelease(ItemStack stack, CallbackInfo ci) {
        if (autoFiring) {
            return;
        }
        var entity = (LivingEntity) (Object) this;
        var charged = false;
        if (entity.getEntityWorld().isClient()) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;
                var mainHandStack = player.getMainHandStack();
                if (AutoFireHook.isApplied(mainHandStack)) {
                    var state = archers$pullProgress(mainHandStack, player);
                    if (state >= 1) {
                        charged = true;
                        // 1 Extra tick to avoid releaseing earlier than server agrees on being charged
                        if (timeHoldingCharged > 1) {
                            // Set weapon charged (in a synchronized way)
                            autoFiring = true;
                            MinecraftClient.getInstance().interactionManager.stopUsingItem(player);
                            // Wait a little before firing (to make sure the server has time to process the packet)
                            ((ItemUseDelay) MinecraftClient.getInstance()).imposeItemUseCD_Archers(2);
                            autoFiring = false;
                        }
                    }
                }
            }
        }
        timeHoldingCharged = charged ? timeHoldingCharged + 1 : 0;
    }
}
