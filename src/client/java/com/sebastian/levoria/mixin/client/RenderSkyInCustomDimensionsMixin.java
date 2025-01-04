package com.sebastian.levoria.mixin.client;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.effects.MoonSkyRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRendering.class)
public abstract class RenderSkyInCustomDimensionsMixin {

    @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
    private void customRenderSky(int phase, float alpha, Tessellator tesselator, MatrixStack matrices, CallbackInfo ci) {
        if(MinecraftClient.getInstance().world.getDimension().effects().equals(Levoria.id("moon"))) {
            ci.cancel();
            MoonSkyRenderer.renderEarth(alpha, tesselator, matrices);
        }
    }
}