package com.sebastian.levoria.network.specific;

import com.sebastian.levoria.Levoria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record ApplyEditedDoorMatC2S(String str, BlockPos pos) implements CustomPayload {
    public static final CustomPayload.Id<ApplyEditedDoorMatC2S> ID = new CustomPayload.Id<>(Levoria.id("edit_door_mat_apply"));
    public static final PacketCodec<RegistryByteBuf, ApplyEditedDoorMatC2S> PACKET_CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, ApplyEditedDoorMatC2S::str, BlockPos.PACKET_CODEC, ApplyEditedDoorMatC2S::pos, ApplyEditedDoorMatC2S::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}