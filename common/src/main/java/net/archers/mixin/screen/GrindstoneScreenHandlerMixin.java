package net.archers.mixin.screen;

import net.archers.item.misc.AutoFireHook;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Grinding a crossbow that carries an Auto-Fire Hook strips the hook back off.
///
/// 1.20.1: `GrindstoneScreenHandler` has no `getOutputStack(ItemStack, ItemStack)` (that was extracted in
/// 1.21) — the result is computed inline in `updateResult()`, so this is a cancellable HEAD inject that
/// writes the result slot itself. The guard is disjoint from SpellEngine's own `updateResult` HEAD inject
/// (which only fires for `#spell_engine:grindable`), so the two never contend for the same stack.
@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin {
    @Shadow @Final private Inventory result;
    @Shadow @Final Inventory input;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void updateResult_HEAD_Archers(CallbackInfo ci) {
        var firstInput = input.getStack(0);
        var secondInput = input.getStack(1);
        if (firstInput.isEmpty() == secondInput.isEmpty()) {
            // Both empty, or both filled: not the single-item "remove the hook" case.
            return;
        }
        var stack = firstInput.isEmpty() ? secondInput : firstInput;
        if (!AutoFireHook.isApplied(stack)) {
            return;
        }
        var output = stack.copy();
        AutoFireHook.remove(output);
        result.setStack(0, output);
        ((ScreenHandler) (Object) this).sendContentUpdates();
        ci.cancel();
    }
}
