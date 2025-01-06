package com.sebastian.levoria.mixin;

import com.sebastian.levoria.config.ConfigManager;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SharedConstants.class) // Target the correct class
public abstract class SharedConstantsMixin {
    @Shadow
    @Mutable
    protected static boolean IS_DEVELOPMENT_VERSION; // Shadow the obfuscated field

    @Inject(
            method = "<clinit>", // Target the class initializer
            at = @At("TAIL")     // Inject at the end of the initializer
    )
    private static void enableDevelopmentMode(CallbackInfo ci) {
        if(ConfigManager.INSTANCE.isDebugMode()) {
            IS_DEVELOPMENT_VERSION = true; // Set the field to true
        }
    }
}
