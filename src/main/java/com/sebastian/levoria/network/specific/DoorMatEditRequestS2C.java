package com.sebastian.levoria.network.specific;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.network.TotemAnimationS2C;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record DoorMatEditRequestS2C(String currentText, BlockPos bePos) implements CustomPayload {
    public static final CustomPayload.Id<DoorMatEditRequestS2C> ID = new CustomPayload.Id<>(Levoria.id("edit_door_mat"));
    public static final PacketCodec<RegistryByteBuf, DoorMatEditRequestS2C> PACKET_CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, DoorMatEditRequestS2C::currentText, BlockPos.PACKET_CODEC, DoorMatEditRequestS2C::bePos, DoorMatEditRequestS2C::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
