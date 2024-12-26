package com.sebastian.levoria.world.custom;


import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sebastian.levoria.Levoria;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class ShadowTreeTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<ShadowTreeTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return fillTrunkPlacerFields(instance).apply(instance, ShadowTreeTrunkPlacer::new);
    });

    public ShadowTreeTrunkPlacer(int baseHeight, int heightRandomA, int heightRandomB) {
        super(baseHeight, heightRandomA, heightRandomB);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        // Replace this with your custom TrunkPlacerType registration
        return Levoria.SHADOW_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        setToDirt(world, replacer, random, startPos.down(), config);

        BlockPos.Mutable mutablePos = startPos.mutableCopy();

        for (int i = 0; i < height; i++) {
            this.getAndSetState(world, replacer, random, mutablePos, config); // Place main trunk log
            mutablePos.move(Direction.UP);

            // Add random branches
            if (random.nextFloat() < 0.25f) { // 25% chance to branch
                Direction branchDirection = Direction.Type.HORIZONTAL.random(random); // Random horizontal direction
                BlockPos branchPos = mutablePos.offset(branchDirection);
                this.getAndSetState(world, replacer, random, branchPos, config);

                // Optional: Extend branch
                if (random.nextFloat() < 0.5f) { // 50% chance for extended branch
                    BlockPos extendedBranchPos = branchPos.offset(branchDirection);
                    this.getAndSetState(world, replacer, random, extendedBranchPos, config);
                }
            }
        }

        // Create a foliage node at the top
        return ImmutableList.of(new FoliagePlacer.TreeNode(mutablePos, 0, false));
    }
}