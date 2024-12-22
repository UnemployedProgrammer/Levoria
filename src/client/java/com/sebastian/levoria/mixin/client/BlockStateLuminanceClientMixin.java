package com.sebastian.levoria.mixin.client;

import com.sebastian.levoria.LevoriaClient;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateLuminanceClientMixin {
	@Shadow public abstract int getLuminance();

	@Shadow @Final private int luminance;

	@Inject(at = @At("HEAD"), method = "getLuminance", cancellable = true)
	private void getLuminance(CallbackInfoReturnable<Integer> cir) {
		if (LevoriaClient.IN_RENDERING) {
			cir.setReturnValue(15);
		} else {
			cir.setReturnValue(luminance);
		}
	}
}