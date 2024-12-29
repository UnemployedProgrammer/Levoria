package com.sebastian.levoria.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class RadiusHelper {
    public static List<ServerPlayerEntity> getPlayersInRadius(ServerWorld world, BlockPos center, double radius) {
        // Create an AABB (Axis-Aligned Bounding Box) around the BlockPos with the given radius
        Box box = new Box(
                center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                center.getX() + radius, center.getY() + radius, center.getZ() + radius
        );
        // Query players within the bounding box
        return world.getPlayers(player -> box.intersects(player.getBoundingBox()));
    }
}
