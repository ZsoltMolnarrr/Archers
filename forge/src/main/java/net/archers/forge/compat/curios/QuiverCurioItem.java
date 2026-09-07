package net.archers.forge.compat.curios;

import net.archers.item.Quivers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/// Forge/Curios flavour of {@link Quivers.QuiverItem}: a quiver that plays the generic equip sound
/// when it is put into (or taken out of) a Curios slot.
///
/// **Curios 5.14.1 (Forge 47) vs Curios 9 (NeoForge).** The NeoForge module attached a standalone
/// `ICurio` capability with `RegisterCapabilitiesEvent.registerItem(CuriosCapability.ITEM, …)`.
/// Forge 47's Curios has no such call — capabilities are attached through `AttachCapabilitiesEvent`,
/// *or* Curios' own event handler auto-attaches one to any stack whose item implements
/// {@link ICurioItem} (`ICurioItem#hasCurioCapability`), with no registration call at all. That is
/// the route taken here (and the one SpellEngine's `SpellHostCurioItem` uses on this line), which is
/// why the behaviour has to live on the item class — hence the {@link Quivers#factory} seam.
///
/// Note the 5.x signatures carry the `ItemStack` explicitly (`onEquip(SlotContext, ItemStack prev,
/// ItemStack stack)`), because one item instance serves every stack.
public class QuiverCurioItem extends Quivers.QuiverItem implements ICurioItem {
    private static final SoundEvent EQUIP_SOUND = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;

    public QuiverCurioItem(@Nullable TagKey<Item> tag, int sizeMultiplier, Settings settings) {
        super(tag, sizeMultiplier, settings);
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(EQUIP_SOUND, 1.0F, 1.0F);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        var entity = slotContext.entity();
        if (entity == null) {
            return;
        }
        var world = entity.getWorld();
        if (world.isClient()                          // the server broadcast below reaches every nearby client
                || entity.age <= 100                  // gear already worn when entering a world/dimension
                || prevStack.isOf(stack.getItem()))   // same quiver, only its contents changed
        {
            return;
        }
        // `null` is ambiguous between World#playSound(Entity, …) and World#playSound(PlayerEntity, …).
        world.playSound((PlayerEntity) null, entity.getBlockPos(), EQUIP_SOUND,
                entity.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
        // Curios' default implementation would play `getEquipSound` a second time, client-side only.
    }
}
