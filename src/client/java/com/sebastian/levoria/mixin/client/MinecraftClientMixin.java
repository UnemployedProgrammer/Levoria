package com.sebastian.levoria.mixin.client;

import com.sebastian.levoria.client_cfg.ClientConfig;
import com.sebastian.levoria.client_cfg.ClientConfigManager;
import com.sebastian.levoria.effects.ScreenShakeEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "close", at = @At("TAIL"))
    public void onShutdown(CallbackInfo ci) {
        ClientConfigManager.write(ClientConfig.INSTANCE);
    }
}

