package net.archers.mixin.screen;

import net.archers.item.misc.AutoFireHook;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
    @Inject(method = "getOutputStack", at = @At("HEAD"), cancellable = true)
    private void getOutputStack_Archers(ItemStack firstInput, ItemStack secondInput, CallbackInfoReturnable<ItemStack> cir) {
        if (firstInput.isEmpty() && secondInput.isEmpty() ) {
            return;
        }
        var output = cir.getReturnValue();
        if (firstInput.isEmpty() || secondInput.isEmpty() ) {
            if (output == null || output.isEmpty()) {
                output = firstInput.isEmpty() ? secondInput : firstInput;
            }
            if (output != null && AutoFireHook.isApplied(output)) {
                var newStack = output.copy();
                AutoFireHook.remove(newStack);
                cir.setReturnValue(newStack);
                cir.cancel();
            }
        }
    }
}
