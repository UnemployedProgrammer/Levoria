package com.sebastian.levoria.block.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.HiddenHunterBlock;
import com.sebastian.levoria.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<HiddenHunterBlockEntity> HIDDEN_HUNTER = register("hidden_hunter_block_entity",
            FabricBlockEntityTypeBuilder.create(HiddenHunterBlockEntity::new, ModBlocks.HIDDEN_HUNTER)
                    .build());

    //METHODS
    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Levoria.getId(name), type);
    }

    public static void registerModBlockEntities() {
        Levoria.LOGGER.info("Initializing Block Entities...");
    }
}
