package com.sebastian.levoria.datagen;

import com.sebastian.levoria.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        BlockStateModelGenerator.BlockTexturePool moonStonePool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.MOON_STONE);
        //blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MOON_STONE);

        moonStonePool.slab(ModBlocks.MOON_STONE_SLAB);
        moonStonePool.stairs(ModBlocks.MOON_STONE_STAIRS);
        moonStonePool.wall(ModBlocks.MOON_STONE_WALL);
        moonStonePool.button(ModBlocks.MOON_STONE_BUTTON);
        moonStonePool.pressurePlate(ModBlocks.MOON_STONE_PRESSURE_PLATE);

        /*
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PINK_GARNET_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PINK_GARNET_DEEPSLATE_ORE);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAGIC_BLOCK);

        pinkGarnetPool.stairs(ModBlocks.PINK_GARNET_STAIRS);
        pinkGarnetPool.slab(ModBlocks.PINK_GARNET_SLAB);

        pinkGarnetPool.button(ModBlocks.PINK_GARNET_BUTTON);
        pinkGarnetPool.pressurePlate(ModBlocks.PINK_GARNET_PRESSURE_PLATE);

        pinkGarnetPool.fence(ModBlocks.PINK_GARNET_FENCE);
        pinkGarnetPool.fenceGate(ModBlocks.PINK_GARNET_FENCE_GATE);
        pinkGarnetPool.wall(ModBlocks.PINK_GARNET_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.PINK_GARNET_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.PINK_GARNET_TRAPDOOR);

        Identifier lampOffIdentifier = TexturedModel.CUBE_ALL.upload(ModBlocks.PINK_GARNET_LAMP, blockStateModelGenerator.modelCollector);
        Identifier lampOnIdentifier = blockStateModelGenerator.createSubModel(ModBlocks.PINK_GARNET_LAMP, "_on", Models.CUBE_ALL, TextureMap::all);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.PINK_GARNET_LAMP)
                .coordinate(BlockStateModelGenerator.createBooleanModelMap(PinkGarnetLampBlock.CLICKED, lampOnIdentifier, lampOffIdentifier)));
         */
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        itemModelGenerator.register(ModBlocks.HIDDEN_HUNTER.asItem(), Models.GENERATED);
        //itemModelGenerator.register(ModItems.RAW_PINK_GARNET, Models.GENERATED);

        /*

        itemModelGenerator.register(ModItems.CAULIFLOWER, Models.GENERATED);
        // itemModelGenerator.register(ModItems.CHISEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.STARLIGHT_ASHES, Models.GENERATED);

        itemModelGenerator.register(ModItems.PINK_GARNET_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GARNET_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GARNET_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GARNET_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PINK_GARNET_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.PINK_GARNET_HAMMER, Models.HANDHELD);

        itemModelGenerator.registerArmor(ModItems.PINK_GARNET_HELMET, Identifier.of(TutorialMod.MOD_ID, "pink_garnet"),
                EquipmentModel.builder().addHumanoidLayers(Identifier.of(TutorialMod.MOD_ID, "pink_garnet")).build(), EquipmentSlot.HEAD);
        itemModelGenerator.registerArmor( ModItems.PINK_GARNET_CHESTPLATE, Identifier.of(TutorialMod.MOD_ID, "pink_garnet"),
                EquipmentModel.builder().addHumanoidLayers(Identifier.of(TutorialMod.MOD_ID, "pink_garnet")).build(), EquipmentSlot.CHEST);
        itemModelGenerator.registerArmor( ModItems.PINK_GARNET_LEGGINGS, Identifier.of(TutorialMod.MOD_ID, "pink_garnet"),
                EquipmentModel.builder().addHumanoidLayers(Identifier.of(TutorialMod.MOD_ID, "pink_garnet")).build(), EquipmentSlot.LEGS);
        itemModelGenerator.registerArmor( ModItems.PINK_GARNET_BOOTS, Identifier.of(TutorialMod.MOD_ID, "pink_garnet"),
                EquipmentModel.builder().addHumanoidLayers(Identifier.of(TutorialMod.MOD_ID, "pink_garnet")).build(), EquipmentSlot.FEET);

        itemModelGenerator.register(ModItems.PINK_GARNET_HORSE_ARMOR, Models.GENERATED);
        itemModelGenerator.register(ModItems.KAUPEN_SMITHING_TEMPLATE, Models.GENERATED);

        itemModelGenerator.register(ModItems.BAR_BRAWL_MUSIC_DISC, Models.GENERATED);
         */
    }
}
