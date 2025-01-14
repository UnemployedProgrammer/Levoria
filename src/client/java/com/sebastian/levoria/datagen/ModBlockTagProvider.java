package com.sebastian.levoria.datagen;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)

                        .add(ModBlocks.MOON_STONE)
                        .add(ModBlocks.MOON_STONE_SLAB)
                        .add(ModBlocks.MOON_STONE_STAIRS)
                        .add(ModBlocks.MOON_STONE_WALL)
                        .add(ModBlocks.MOON_STONE_PRESSURE_PLATE)
                        .add(ModBlocks.MOON_STONE_BUTTON)

                        .add(ModBlocks.MOON_BRICKS)
                        .add(ModBlocks.MOON_BRICKS_SLAB)
                        .add(ModBlocks.MOON_BRICKS_STAIRS)
                        .add(ModBlocks.MOON_BRICKS_WALL)
                        .add(ModBlocks.MOON_BRICKS_PRESSURE_PLATE)
                        .add(ModBlocks.MOON_BRICKS_BUTTON)
        //                .add(ModBlocks.RAW_PINK_GARNET_BLOCK)
        //                .add(ModBlocks.PINK_GARNET_ORE)
        //                .add(ModBlocks.PINK_GARNET_DEEPSLATE_ORE)
        //                .add(ModBlocks.MAGIC_BLOCK)
        ;

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.MOON_STONE_WALL)
                .add(ModBlocks.MOON_BRICKS_WALL)
        ;

        getOrCreateTagBuilder(BlockTags.BUTTONS)
                .add(ModBlocks.MOON_STONE_BUTTON)
                .add(ModBlocks.MOON_BRICKS_BUTTON);

        getOrCreateTagBuilder(BlockTags.OVERWORLD_CARVER_REPLACEABLES)
                .add(ModBlocks.MOON_STONE)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.SHADOW_WOOD_LOG)
                .add(ModBlocks.SHADOW_WOOD_WOOD)
                .add(ModBlocks.STRIPPED_SHADOW_WOOD_LOG)
                .add(ModBlocks.STRIPPED_SHADOW_WOOD_WOOD)
        ;

        getOrCreateTagBuilder(BlockTags.FENCES).add(ModBlocks.SHADOW_FENCE);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(ModBlocks.SHADOW_FENCE_GATE);

        //getOrCreateTagBuilder(ModTags.Items.ORES)
        //                        .add(Blocks.COAL_ORE)
        //                        .add(Blocks.DEEPSLATE_COAL_ORE)
        //
        //                        .add(Blocks.IRON_ORE)
        //                        .add(Blocks.DEEPSLATE_IRON_ORE)
        //
        //                        .add(Blocks.GOLD_ORE)
        //                        .add(Blocks.DEEPSLATE_GOLD_ORE)
        //
        //                        .add(Blocks.LAPIS_ORE)
        //                        .add(Blocks.DEEPSLATE_LAPIS_ORE)
        //
        //                        .add(Blocks.REDSTONE_ORE)
        //                        .add(Blocks.DEEPSLATE_REDSTONE_ORE)
        //
        //                        .add(Blocks.DIAMOND_ORE)
        //                        .add(Blocks.DEEPSLATE_DIAMOND_ORE)
        //
        //                        .add(Blocks.NETHER_QUARTZ_ORE)
        //
        //                        .add(Blocks.NETHER_GOLD_ORE)
        //
        //                        .add(Blocks.ANCIENT_DEBRIS)
        //        ;


        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                //.add(ModBlocks.PINK_GARNET_DEEPSLATE_ORE);
        ;
    }
}
