package net.archers.mixin.client;

import net.archers.item.AutoFireHook;
import net.archers.client.ItemUseDelay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private int lastLoaded = 0;

    @Inject(method = "tickItemStackUsage", at = @At("HEAD"))
    private void asd(ItemStack stack, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient() && stack.getUseAction() == UseAction.CROSSBOW) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;
                var mainHandStack = player.getMainHandStack();
                if (AutoFireHook.isApplied(mainHandStack)) {
                    var predicate = ModelPredicateProviderRegistry.get(mainHandStack.getItem(), new Identifier("pull"));
                    if (predicate != null) {
                        var state = predicate.call(mainHandStack, (ClientWorld) entity.getWorld(), player, 1234);
                        if (state >= 1) {
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
}
