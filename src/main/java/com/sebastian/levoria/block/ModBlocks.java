package com.sebastian.levoria.block;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModItemGroups;
import com.sebastian.levoria.util.AdvancedRegisterBlock;
import com.sebastian.levoria.world.tree.ModSaplingGenerators;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import java.lang.reflect.InvocationTargetException;

public class ModBlocks {

    //    public static final Block EXAMPLE = registerBlock("example",
    //            new Block(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.getId("example"))).strength(4f)
    //                    .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK))); //CLASSIC EXAMPLE

    //public static final Block EXAMPLE = registerEasyBlock("example", Block.class, new AbstractBlock.Settings()); //DOES NOT WORK :( | MUST USE CLASSIC EXAMPLE!
    //public static final Block EXAMPLE = registerEasyBlock("example", Block.class, new Block.Settings());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final Block MOON_STONE = registerBlock("moon_stone",
            new Block(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("moon_stone"))).strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));

    public static final Block MOON_BRICKS = registerBlock("moon_bricks",
            new Block(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("moon_bricks"))).strength(3f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));


    public static final HiddenHunterBlock HIDDEN_HUNTER = AdvancedRegisterBlock.registerWithItem("hidden_hunter",
            new HiddenHunterBlock(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("hidden_hunter"))).nonOpaque().strength(30f)
                    .sounds(BlockSoundGroup.LILY_PAD).noBlockBreakParticles()));

    public static final DoorMatBlock DOORMAT = AdvancedRegisterBlock.registerWithItem("doormat",
            new DoorMatBlock(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("doormat"))).nonOpaque().strength(1f)
                    .sounds(BlockSoundGroup.WOOL)));

    public static final MoonBerryBush MOON_BERRY_BUSH = AdvancedRegisterBlock.register("moon_berry_bush",
            new MoonBerryBush(AbstractBlock.Settings.copy(Blocks.SWEET_BERRY_BUSH).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("moon_berry_bush")))));

    //CUSTOM NON_BLOCK BLOCKS (Stairs, etc...)

    private static AbstractBlock.Settings getMoonBlockNonBlockSettings(String name) {
        return AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id(name))).strength(1.5f).requiresTool().sounds(BlockSoundGroup.STONE).mapColor(DyeColor.WHITE);
    }

    public static final Block MOON_STONE_STAIRS = registerBlock("moon_stone_stairs",
            new StairsBlock(ModBlocks.MOON_STONE.getDefaultState(),
                    getMoonBlockNonBlockSettings("moon_stone_stairs")));

    public static final Block MOON_STONE_SLAB = registerBlock("moon_stone_slab",
            new SlabBlock(getMoonBlockNonBlockSettings("moon_stone_slab")));

    public static final Block MOON_STONE_BUTTON = registerBlock("moon_stone_button",
            new ButtonBlock(BlockSetType.IRON, 12, getMoonBlockNonBlockSettings("moon_stone_button").noCollision()));

    public static final Block MOON_STONE_PRESSURE_PLATE = registerBlock("moon_stone_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, getMoonBlockNonBlockSettings("moon_stone_pressure_plate")));

    public static final Block MOON_STONE_WALL = registerBlock("moon_stone_wall",
            new WallBlock(getMoonBlockNonBlockSettings("moon_stone_wall")));

    public static final Block MOON_BRICKS_STAIRS = registerBlock("moon_bricks_stairs",
            new StairsBlock(ModBlocks.MOON_STONE.getDefaultState(),
                    getMoonBlockNonBlockSettings("moon_bricks_stairs")));

    public static final Block MOON_BRICKS_SLAB = registerBlock("moon_bricks_slab",
            new SlabBlock(getMoonBlockNonBlockSettings("moon_bricks_slab")));

    public static final Block MOON_BRICKS_BUTTON = registerBlock("moon_bricks_button",
            new ButtonBlock(BlockSetType.IRON, 20, getMoonBlockNonBlockSettings("moon_bricks_button").noCollision()));

    public static final Block MOON_BRICKS_PRESSURE_PLATE = registerBlock("moon_bricks_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, getMoonBlockNonBlockSettings("moon_bricks_pressure_plate")));

    public static final Block MOON_BRICKS_WALL = registerBlock("moon_bricks_wall",
            new WallBlock(getMoonBlockNonBlockSettings("moon_bricks_wall")));

    public static final Block SHADOW_WOOD_LOG = registerBlock("shadowwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadowwood_log")))));
    public static final Block SHADOW_WOOD_WOOD = registerBlock("shadowwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadowwood_wood")))));
    public static final Block STRIPPED_SHADOW_WOOD_LOG = registerBlock("stripped_shadowwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("stripped_shadowwood_log")))));
    public static final Block STRIPPED_SHADOW_WOOD_WOOD = registerBlock("stripped_shadowwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("stripped_shadowwood_wood")))));

    public static final Block SHADOW_WOOD_PLANKS = registerBlock("shadowwood_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadowwood_planks")))));
    public static final Block SHADOW_WOOD_LEAVES = registerBlock("shadowwood_leaves",
            new LeavesBlock(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadowwood_leaves")))));

    public static final Block SHADOW_WOOD_SAPLING = registerBlock("shadowwood_sapling",
            new SaplingBlock(ModSaplingGenerators.SHADOWWOOD, AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadowwood_sapling"))))); //ModSaplingGenerators.DRIFTWOOD


    public static final Block SHADOW_STAIRS = registerBlock("shadow_stairs",
            new StairsBlock(ModBlocks.SHADOW_WOOD_PLANKS.getDefaultState(),
                    AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_stairs")))
                            .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD)));
    public static final Block SHADOW_SLAB = registerBlock("shadow_slab",
            new SlabBlock(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_slab")))
                    .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD)));

    public static final Block SHADOW_BUTTON = registerBlock("shadow_button",
            new ButtonBlock(BlockSetType.IRON, 2, AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_button")))
                    .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD).noCollision()));
    public static final Block SHADOW_PRESSURE_PLATE = registerBlock("shadow_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_pressure_plate")))
                    .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD)));

    public static final Block SHADOW_FENCE = registerBlock("shadow_fence",
            new FenceBlock(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_fence")))
                    .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD)));
    public static final Block SHADOW_FENCE_GATE = registerBlock("shadow_fence_gate",
            new FenceGateBlock(WoodType.ACACIA, AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_fence_gate")))
                    .strength(2f).requiresTool().sounds(BlockSoundGroup.WOOD)));

    public static final Block SHADOW_DOOR = registerBlock("shadow_door",
            new DoorBlock(BlockSetType.OAK, AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_door")))
                    .strength(2f).requiresTool().nonOpaque().sounds(BlockSoundGroup.WOOD)));
    public static final Block SHADOW_TRAPDOOR = registerBlock("shadow_trapdoor",
            new TrapdoorBlock(BlockSetType.OAK, AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id("shadow_trapdoor")))
                    .strength(2f).requiresTool().nonOpaque().sounds(BlockSoundGroup.WOOD)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Levoria.id(name), block);
    }

    private static Block registerEasyBlock(String name, Class<? extends Block> block, AbstractBlock.Settings settings) {
        try {
            return Registry.register(Registries.BLOCK, Levoria.id(name), block.getConstructor(AbstractBlock.Settings.class).newInstance(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.id(name)))));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerBlockItem(String name, Block block) {
        BlockItem b = Registry.register(Registries.ITEM, Levoria.id(name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id(name)))));
        ModItemGroups.add(b);
    }

    public static void registerModBlocks() {
        Levoria.LOGGER.info("Registering Mod Blocks for " + Levoria.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            //entries.add(ModBlocks.EXAMPLE);
        });
    }
}
