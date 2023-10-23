package net.archers.mixin.client.autofire;

import net.archers.client.util.ItemUseDelay;
import net.archers.item.misc.AutoFireHook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private int timeHoldingCharged = 0;
    @Inject(method = "tickItemStackUsage", at = @At("HEAD"))
    private void asd(ItemStack stack, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        var charged = false;
        if (entity.getWorld().isClient()) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;
                var mainHandStack = player.getMainHandStack();
                if (AutoFireHook.isApplied(mainHandStack)) {
                    var predicate = ModelPredicateProviderRegistry.get(mainHandStack.getItem(), new Identifier("pull"));
                    if (predicate != null) {
                        var state = predicate.call(mainHandStack, (ClientWorld) entity.getWorld(), player, 1234);
                        if (state >= 1) {
                            charged = true;
                            // 1 Extra tick to avoid releaseing earlier than server agrees on being charged
                            if (timeHoldingCharged > 1) {
                                // Set weapon charged (in a synchronized way)
                                MinecraftClient.getInstance().interactionManager.stopUsingItem(player);
                                // Wait a little before firing (to make sure the server has time to process the packet)
                                ((ItemUseDelay) MinecraftClient.getInstance()).imposeItemUseCD_Archers(2);
                            }
                        }
                    }
                }
            }
        }
        timeHoldingCharged = charged ? timeHoldingCharged + 1 : 0;
    }
}
