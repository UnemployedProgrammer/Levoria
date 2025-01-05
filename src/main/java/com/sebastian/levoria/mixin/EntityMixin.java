package com.sebastian.levoria.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EntityMixin {
	@Inject(at = @At("HEAD"), method = "getEffectiveGravity", cancellable = true)
	private void getGravity(CallbackInfoReturnable<Double> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;

		boolean bl = entity.getVelocity().y <= 0.0;
		if(bl && entity.getWorld().getDimension().effects().toString().equals("levoria:moon")) {
			entity.fallDistance = 0.0F;
			cir.setReturnValue(Math.min(entity.getFinalGravity(), 0.01));
		}
	}

	@Inject(at = @At("HEAD"), method = "getJumpBoostVelocityModifier", cancellable = true)
	private void jumpBoost(CallbackInfoReturnable<Float> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;

		if(entity.getWorld().getDimension().effects().toString().equals("levoria:moon")) {
			cir.setReturnValue(0.5F);
		}
	}
}