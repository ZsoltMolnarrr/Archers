package net.archers.mixin.screen;

import net.archers.item.misc.AutoFireHook;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    // Apply AutoFireHook

    @Inject(method = "updateResult", at = @At(value = "RETURN"))
    private void updateResult_Inject(CallbackInfo ci) {
        // System.out.println("AFH mixin: 1");
        var input = this.input;
        ItemStack input1 = input.getStack(0);
        ItemStack input2 = input.getStack(1);
        if (input1.isEmpty() || input2.isEmpty()) {
            // System.out.println("AFH mixin: 1 exit");
            return;
        }

        // System.out.println("AFH mixin: 2");
        ItemStack afh = null;
        ItemStack crossbow = null;
        if (input1.isOf(AutoFireHook.item) && AutoFireHook.isApplicable(input2)) {
            afh = input1;
            crossbow = input2;
        } else if (AutoFireHook.isApplicable(input1) && input2.isOf(AutoFireHook.item)) {
            afh = input2;
            crossbow = input1;
        }
        if (afh == null || crossbow == null || !AutoFireHook.isApplicable(crossbow) ) {
            return;
        }

        // System.out.println("AFH mixin: 3");

        var afhCrossbow = crossbow.copy();
        AutoFireHook.apply(afhCrossbow);
        // System.out.println("AFH mixin: 4, Setting result: " + afhCrossbow);

        this.output.setStack(0, afhCrossbow);
        levelCost.set(1);
        sendContentUpdates();
    }
}
