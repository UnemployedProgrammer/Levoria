package com.sebastian.levoria.datagen;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.REACH_ENCHANTABLE)
        //                .add(Items.COAL)
        //                .add(Items.STICK)
                        .add(ModItems.DOWSING_ROD);

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.SHADOW_WOOD_LOG.asItem())
                .add(ModBlocks.SHADOW_WOOD_WOOD.asItem())
                .add(ModBlocks.STRIPPED_SHADOW_WOOD_LOG.asItem())
                .add(ModBlocks.STRIPPED_SHADOW_WOOD_WOOD.asItem())
        ;

        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.SHADOW_WOOD_PLANKS.asItem())
        ;
    }
}
