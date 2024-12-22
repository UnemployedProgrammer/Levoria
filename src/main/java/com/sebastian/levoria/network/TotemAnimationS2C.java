package com.sebastian.levoria.network;

import com.sebastian.levoria.Levoria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public record TotemAnimationS2C(Identifier block) implements CustomPayload {
    public static final CustomPayload.Id<TotemAnimationS2C> ID = new Id<>(Levoria.getId("totem_animation"));
    public static final PacketCodec<RegistryByteBuf, TotemAnimationS2C> PACKET_CODEC =
            PacketCodec.tuple(Identifier.PACKET_CODEC, TotemAnimationS2C::block, TotemAnimationS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static Block toBlock(Identifier id) {
        return Registries.BLOCK.get(id);
    }

    public static Identifier toId(Block block) {
        return Registries.BLOCK.getId(block);
    }
}
