package com.sebastian.levoria.block;

import com.sebastian.levoria.Levoria;
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

import java.lang.reflect.InvocationTargetException;

public class ModBlocks {

    //    public static final Block EXAMPLE = registerBlock("example",
    //            new Block(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.getId("example"))).strength(4f)
    //                    .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK))); //CLASSIC EXAMPLE

    //public static final Block EXAMPLE = registerEasyBlock("example", Block.class, new AbstractBlock.Settings()); //DOES NOT WORK :( | MUST USE CLASSIC EXAMPLE!
    //public static final Block EXAMPLE = registerEasyBlock("example", Block.class, new Block.Settings());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final Block MOON_STONE = registerBlock("moon_stone",
            new Block(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.getId("moon_stone"))).strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));

    //CUSTOM NON_BLOCK BLOCKS (Stairs, etc...)

    private static AbstractBlock.Settings getMoonBlockNonBlockSettings(String name) {
        return AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.getId(name))).strength(2f).requiresTool();
    }

    public static final Block MOON_STONE_STAIRS = registerBlock("moon_stone_stairs",
            new StairsBlock(ModBlocks.MOON_STONE.getDefaultState(),
                    getMoonBlockNonBlockSettings("moon_stone_stairs")));

    public static final Block MOON_STONE_SLAB = registerBlock("moon_stone_slab",
            new SlabBlock(getMoonBlockNonBlockSettings("moon_stone_slab")));

    public static final Block MOON_STONE_BUTTON = registerBlock("moon_stone_button",
            new ButtonBlock(BlockSetType.IRON, 2, getMoonBlockNonBlockSettings("moon_stone_button").noCollision()));

    public static final Block MOON_STONE_PRESSURE_PLATE = registerBlock("moon_stone_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON, getMoonBlockNonBlockSettings("moon_stone_pressure_plate")));

    public static final Block MOON_STONE_WALL = registerBlock("moon_stone_wall",
            new WallBlock(getMoonBlockNonBlockSettings("moon_stone_wall")));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Levoria.getId(name), block);
    }

    private static Block registerEasyBlock(String name, Class<? extends Block> block, AbstractBlock.Settings settings) {
        try {
            return Registry.register(Registries.BLOCK, Levoria.getId(name), block.getConstructor(AbstractBlock.Settings.class).newInstance(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Levoria.getId(name)))));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Levoria.getId(name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.getId(name)))));
    }

    public static void registerModBlocks() {
        Levoria.LOGGER.info("Registering Mod Blocks for " + Levoria.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            //entries.add(ModBlocks.EXAMPLE);
        });
    }
}
