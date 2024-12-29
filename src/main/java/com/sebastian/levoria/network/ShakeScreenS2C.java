package com.sebastian.levoria.network;

import com.sebastian.levoria.Levoria;
import net.minecraft.block.Block;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record ShakeScreenS2C(int duration) implements CustomPayload {

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
