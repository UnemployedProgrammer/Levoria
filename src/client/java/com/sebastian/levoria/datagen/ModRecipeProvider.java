package com.sebastian.levoria.datagen;

import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                /*
                List<ItemConvertible> PINK_GARNET_SMELTABLES = List.of(ModItems.RAW_PINK_GARNET, ModBlocks.PINK_GARNET_ORE,
                        ModBlocks.PINK_GARNET_DEEPSLATE_ORE);

                offerSmelting(PINK_GARNET_SMELTABLES, RecipeCategory.MISC, ModItems.PINK_GARNET, 0.25f, 200, "pink_garnet");
                offerBlasting(PINK_GARNET_SMELTABLES, RecipeCategory.MISC, ModItems.PINK_GARNET, 0.25f, 100, "pink_garnet");

                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS, ModItems.PINK_GARNET, RecipeCategory.DECORATIONS, ModBlocks.PINK_GARNET_BLOCK);

                createShaped(RecipeCategory.MISC, ModBlocks.RAW_PINK_GARNET_BLOCK)
                        .pattern("RRR")
                        .pattern("RRR")
                        .pattern("RRR")
                        .input('R', ModItems.RAW_PINK_GARNET)
                        .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 9)
                        .input(ModBlocks.RAW_PINK_GARNET_BLOCK)
                        .criterion(hasItem(ModBlocks.RAW_PINK_GARNET_BLOCK), conditionsFromItem(ModBlocks.RAW_PINK_GARNET_BLOCK))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 32)
                        .input(ModBlocks.MAGIC_BLOCK)
                        .criterion(hasItem(ModBlocks.MAGIC_BLOCK), conditionsFromItem(ModBlocks.MAGIC_BLOCK))
                        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(TutorialMod.MOD_ID, "raw_pink_garnet_from_magic_block")));

                offerSmithingTrimRecipe(ModItems.KAUPEN_SMITHING_TEMPLATE, RegistryKey.of(RegistryKeys.RECIPE,
                        Identifier.ofVanilla(getItemPath(ModItems.KAUPEN_SMITHING_TEMPLATE) + "_smithing_trim")));
                 */

                createShaped(RecipeCategory.MISC, ModItems.DOWSING_ROD)
                        .pattern("ABC")
                        .pattern("DEF")
                        .pattern("GHI")
                        .input('A', Blocks.COAL_ORE.asItem())
                        .input('B', Blocks.IRON_ORE.asItem())
                        .input('C', Blocks.GOLD_ORE.asItem())
                        .input('D', Blocks.DEEPSLATE_DIAMOND_ORE.asItem())
                        .input('E', Blocks.DEEPSLATE_REDSTONE_ORE.asItem())
                        .input('F', Blocks.DEEPSLATE_LAPIS_ORE.asItem())
                        .input('G', Blocks.NETHER_QUARTZ_ORE.asItem())
                        .input('H', Blocks.NETHER_GOLD_ORE.asItem())
                        .input('I', Blocks.ANCIENT_DEBRIS.asItem())
                        .criterion(hasItem(ModItems.DOWSING_ROD), conditionsFromItem(ModItems.DOWSING_ROD))
                        .offerTo(exporter);
            }
        };
    }


    @Override
    public String getName() {
        return "Levoria Recipes";
    }
}
