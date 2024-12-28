package com.sebastian.levoria.mixin.client;

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

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "update", at = @At("TAIL"))
    public void onSetup(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        if (!ScreenShakeEffect.INSTANCE.isShaking()) {
            return;
        }

        var MC = MinecraftClient.getInstance();
        float intensity = ScreenShakeEffect.INSTANCE.getShakeIntensity();
        var player = MC.player;

        if (player == null) {
            return;
        }

        var shakeRotation = new Vector3f();
        var shakeOffset = new Vector3f();

        // Add simple shake logic
        shakeRotation.set(
                (float) (Math.random() - 0.5) * intensity,
                (float) (Math.random() - 0.5) * intensity,
                (float) (Math.random() - 0.5) * intensity
        );
        shakeOffset.set(
                (float) (Math.random() - 0.5) * intensity,
                (float) (Math.random() - 0.5) * intensity,
                (float) (Math.random() - 0.5) * intensity
        );

        var camera = MC.gameRenderer.getCamera();
        var inverseRotation = new Quaternionf(camera.getRotation()).conjugate();

        if (shakeRotation.lengthSquared() > 0) {
            shakeRotation.rotate(inverseRotation);

            float rotationFactor = 10F;
            float rotationX = shakeRotation.x() * rotationFactor;
            float rotationY = shakeRotation.y() * rotationFactor;
            float rotationZ = shakeRotation.z() * rotationFactor;

            camera.getRotation().mul(new Quaternionf()
                    .rotateX(-rotationX * (float) (Math.PI / 180F))
                    .rotateY(-rotationY * (float) (Math.PI / 180F))
                    .rotateZ(-rotationZ * (float) (Math.PI / 180F)));
        }

        if (shakeOffset.lengthSquared() > 0) {
            shakeOffset.rotate(inverseRotation);
            camera.getPos().add(-shakeOffset.z(), shakeOffset.y(), shakeOffset.x());
        }
    }
}

