package com.sebastian.levoria.block.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.network.specific.DoorMatEditRequestS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.UUID;


public class DoorMatBlockEntity extends BlockEntity {

    private String message = "translate->doormat.levoria.welcome";

    public DoorMatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DOORMAT_BLOCK, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putString("message", message);
        super.writeNbt(nbt, registries);
    }

    private void updateListeners() {
        this.markDirty();
        this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public void setMessage(String msg) {
        updateListeners();
        this.message = msg;
    }


    public void setRandomMessage() {
        // Example messages
        List<String> messages = List.of(
                "Quantum Flux",
                "Chaos Theory",
                "Space-Time",
                "Hyper Drive",
                "Neural Sync",
                "Biofusion",
                "Void Echo",
                "Stellar Drift",
                "Graviton Burst",
                "Plasma Forge"
        );

        // Get a random message
        Random random = new Random();
        setMessage(messages.get(random.nextInt(messages.size())));
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if(nbt.contains("message")) {
            message = nbt.getString("message");
        }
        super.readNbt(nbt, registries);
    }

    public String getMessage() {
        return message;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public void editScreenFor(UUID serverPlayer) {
        try {
            ServerPlayNetworking.send(world.getServer().getPlayerManager().getPlayer(serverPlayer), new DoorMatEditRequestS2C(getMessage(), pos));
        } catch (Exception e) {

            Levoria.LOGGER.warn("Couldn't send doormat edit request to {}", serverPlayer);
        }
    }
}
