package net.archers.neoforge.compat.curios;

import net.archers.item.Quivers;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * Quivers are plain bundle items, so Curios has no capability to ask about them and
 * equipping one is silent. Attaching a minimal {@link ICurio} gives them the same
 * generic equip sound relics play, without touching the item class itself.
 */
public class QuiverCurios {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var entry : Quivers.entries) {
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new QuiverCurio(stack), entry.item());
        }
    }

    private record QuiverCurio(ItemStack stack) implements ICurio {
        @Override
        public ItemStack getStack() {
            return this.stack;
        }

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack) {
            var entity = slotContext.entity();
            if (entity == null) {
                return;
            }
            var world = entity.getEntityWorld();
            if (world.isClient()                             // the server broadcast below reaches every nearby client
                    || entity.age <= 100                     // gear already worn when entering a world/dimension
                    || prevStack.isOf(this.stack.getItem())) // same quiver, only its contents changed
            {
                return;
            }
            world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(),
                    entity.getSoundCategory(), 1.0F, 1.0F);
        }

        @Override
        public void onEquipFromUse(SlotContext slotContext) {
            // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
        }
    }
}
