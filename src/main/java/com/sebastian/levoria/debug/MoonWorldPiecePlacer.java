package com.sebastian.levoria.debug;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class MoonWorldPiecePlacer {
    private final World world;
    private final BlockPos bottomLeftCorner;
    private final int sizeX, sizeY, sizeZ;

    public MoonWorldPiecePlacer(World world, BlockPos bottomLeftCorner, int sizeX, int sizeY, int sizeZ) {
        this.world = world;
        this.bottomLeftCorner = bottomLeftCorner;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    private void placeBlock(Block block, BlockPos pos) {
        world.getServer().execute(() -> {
            world.setBlockState(pos, block.getDefaultState());
        });
    }

    private void destroyBlock(BlockPos pos) {
        world.getServer().execute(() -> {
            world.breakBlock(pos, false);
        });
    }

    private void placeMoonStone(BlockPos pos) {
        world.getServer().execute(() -> {
            world.setBlockState(pos, ModBlocks.MOON_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
        });
    }

    public void generateWorld() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int x = 0; x < sizeX ; x++) {
                        for (int y = 0; y < sizeY ; y++) {
                            for (int z = 0; z < sizeZ ; z++) {
                                placeMoonStone(bottomLeftCorner.add(x, y, z));
                                Thread.sleep(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    Levoria.LOGGER.warn("Error while generating...");
                    Levoria.LOGGER.warn(e.toString());
                }
            }
        }, "placeMoonStructureThread");
        t.start();
    }

    public void clearWorld() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int x = 0; x < sizeX ; x++) {
                        for (int y = 0; y < sizeY ; y++) {
                            for (int z = 0; z < sizeZ ; z++) {
                                destroyBlock(bottomLeftCorner.add(x, y, z));
                                Thread.sleep(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    Levoria.LOGGER.warn("Error while clearing...");
                    Levoria.LOGGER.warn(e.toString());
                }
            }
        }, "clearMoonStructureThread#" + UUID.randomUUID().toString());
        t.start();
    }
}
