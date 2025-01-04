package com.sebastian.levoria.network;

import com.sebastian.levoria.Levoria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record HighlightBlockS2C(BlockPos block, int ticks) implements CustomPayload {
    public static final Id<HighlightBlockS2C> ID = new Id<>(Levoria.id("highlight_block"));
    public static final PacketCodec<RegistryByteBuf, HighlightBlockS2C> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, HighlightBlockS2C::block, PacketCodecs.INTEGER, HighlightBlockS2C::ticks,HighlightBlockS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
