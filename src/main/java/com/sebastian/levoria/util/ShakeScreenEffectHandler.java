package com.sebastian.levoria.util;

import com.sebastian.levoria.network.ShakeScreenS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ShakeScreenEffectHandler {
    public static void shakePlayer(ServerPlayerEntity serverPlayerEntity, int duration, float strength) {
        ServerPlayNetworking.send(serverPlayerEntity, new ShakeScreenS2C(duration, strength));
    }

    public static void stopShakingPlayer(ServerPlayerEntity serverPlayerEntity) {
        ServerPlayNetworking.send(serverPlayerEntity, new ShakeScreenS2C(0, 0f));
    }
}
