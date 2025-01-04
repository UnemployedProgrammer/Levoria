package com.sebastian.levoria.network;

import com.sebastian.levoria.Levoria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record DebugRenderingS2C(String action, String param1, String param2, String param3, String param4) implements CustomPayload {
    public static final Id<DebugRenderingS2C> ID = new Id<>(Levoria.id("debug_rendering"));
    public static final PacketCodec<RegistryByteBuf, DebugRenderingS2C> PACKET_CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, DebugRenderingS2C::action, PacketCodecs.STRING, DebugRenderingS2C::param1, PacketCodecs.STRING, DebugRenderingS2C::param2, PacketCodecs.STRING, DebugRenderingS2C::param3, PacketCodecs.STRING, DebugRenderingS2C::param4, DebugRenderingS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
