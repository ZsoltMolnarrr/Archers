package net.archers.mixin.client;

import net.archers.client.ItemUseDelay;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements ItemUseDelay {
    @Shadow private int itemUseCooldown;
    @Override
    public void imposeItemUseCD_Archers(int ticks) {
        itemUseCooldown = ticks;
    }
}
