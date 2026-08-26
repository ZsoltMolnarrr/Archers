package net.archers.mixin.client.autofire;

import net.archers.client.util.ItemUseDelay;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MinecraftClientMixin implements ItemUseDelay {
    @Shadow private int rightClickDelay;
    @Override
    public void imposeItemUseCD_Archers(int ticks) {
        rightClickDelay = ticks;
    }
}
