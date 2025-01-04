package com.sebastian.levoria.network;

import com.sebastian.levoria.Levoria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ShakeScreenS2C(int duration, float strength) implements CustomPayload {
    public static final Id<ShakeScreenS2C> ID = new Id<>(Levoria.id("shake_screen"));
    public static final PacketCodec<RegistryByteBuf, ShakeScreenS2C> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, ShakeScreenS2C::duration,
                    PacketCodecs.FLOAT, ShakeScreenS2C::strength,
                    ShakeScreenS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
