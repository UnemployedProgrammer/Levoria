package com.sebastian.levoria.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ArmorEquipMixin {

	/*
	@Shadow
	private boolean firstUpdate;

	@Shadow
	protected boolean isArmorSlot(EquipmentSlot slot) {//DUMMY BLOCK}

@Inject(at = @At("HEAD"), method = "onEquipStack", cancellable = true)
private void equipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
	LivingEntity entity = (LivingEntity) (Object) this;

	if (!entity.getWorld().isClient() && !entity.isSpectator()) {
		boolean bl = newStack.isEmpty() && oldStack.isEmpty();
		if (!bl && !ItemStack.areItemsAndComponentsEqual(oldStack, newStack) && !entity.firstUpdate) {
			EquippableComponent equippableComponent = newStack.get(DataComponentTypes.EQUIPPABLE);

			if (this.isArmorSlot(slot)) {
				entity.emitGameEvent(equippableComponent != null ? GameEvent.EQUIP : GameEvent.UNEQUIP);
			}
		}
	}
}
	 */

}