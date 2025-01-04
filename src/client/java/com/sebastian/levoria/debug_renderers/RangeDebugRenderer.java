package com.sebastian.levoria.debug_renderers;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.LevoriaClient;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RangeDebugRenderer {
    public static Boolean rendering = false;
    public static BlockPos center = new BlockPos(0, 0, 0);
    public static Integer radius = 1;

    public static void shouldBegin(BlockPos c, Integer r) {
        center = c;
        radius = r;
        rendering = true;
    }
    public static void shouldStop() {
        rendering = false;
    }

    public static void render(WorldRenderContext ctx) {
        if(rendering) {
            for (BlockPos pos : getHollowSphereAroundPos(center, radius)) {
                LevoriaClient.renderBlock(ctx, Blocks.RED_CONCRETE.getDefaultState(), pos);
            }
        }
    }

    public static List<BlockPos> getHollowSphereAroundPos(BlockPos pos, int radius) {
        List<BlockPos> positions = new ArrayList<>();

        int radiusSquared = radius * radius;

        // Iterate through a cube containing the sphere
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Calculate the distance from the center
                    int distanceSquared = x * x + y * y + z * z;

                    // Add only the positions that are exactly on the surface of the sphere
                    if (distanceSquared == radiusSquared) {
                        BlockPos offset = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        positions.add(offset);
                    }
                }
            }
        }

        return positions;
    }

}
