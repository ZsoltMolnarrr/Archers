package net.archers.mixin.client.compat;

import net.archers.client.util.ArchersTooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.projectile_damage.client.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TooltipHelper.class)
public class ProjectileDamageTooltipMixin {
    @Inject(method = "updateTooltipText", at = @At("TAIL"))
    private static void addPullTime(ItemStack itemStack, List<Text> lines, CallbackInfo ci) {
        ArchersTooltip.addPullTime(itemStack, lines);
    }
}
