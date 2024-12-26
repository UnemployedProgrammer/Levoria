package com.sebastian.levoria.datagen;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.MoonBerryBush;
import com.sebastian.levoria.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
        this.registryLookup = registryLookup;
    }

    @Override
    public void generate() {
        try {
            RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.get().getOrThrow(RegistryKeys.ENCHANTMENT);
            addDrop(ModBlocks.MOON_STONE);
            addDrop(ModBlocks.MOON_STONE_SLAB, slabDrops(ModBlocks.MOON_STONE_SLAB));
            addDrop(ModBlocks.MOON_STONE_STAIRS);
            addDrop(ModBlocks.MOON_STONE_WALL);
            addDrop(ModBlocks.MOON_STONE_PRESSURE_PLATE);
            addDrop(ModBlocks.MOON_STONE_BUTTON);

            addDrop(ModBlocks.MOON_BRICKS);
            addDrop(ModBlocks.MOON_BRICKS_SLAB, slabDrops(ModBlocks.MOON_BRICKS_SLAB));
            addDrop(ModBlocks.MOON_BRICKS_STAIRS);
            addDrop(ModBlocks.MOON_BRICKS_WALL);
            addDrop(ModBlocks.MOON_BRICKS_PRESSURE_PLATE);
            addDrop(ModBlocks.MOON_BRICKS_BUTTON);
            //addDropWithSilkTouch(ModBlocks.HIDDEN_HUNTER, ModBlocks.HIDDEN_HUNTER);

            addDrop(ModBlocks.SHADOW_WOOD_LOG);
            addDrop(ModBlocks.SHADOW_WOOD_WOOD);
            addDrop(ModBlocks.STRIPPED_SHADOW_WOOD_LOG);
            addDrop(ModBlocks.STRIPPED_SHADOW_WOOD_WOOD);
            addDrop(ModBlocks.SHADOW_WOOD_PLANKS);
            addDrop(ModBlocks.SHADOW_WOOD_SAPLING);
            addDrop(ModBlocks.SHADOW_WOOD_LEAVES, leavesDrops(ModBlocks.SHADOW_WOOD_LEAVES, ModBlocks.SHADOW_WOOD_SAPLING, 0.125f));


            this.addDrop(ModBlocks.MOON_BERRY_BUSH,
                    block -> this.applyExplosionDecay(
                            block,LootTable.builder().pool(LootPool.builder().conditionally(
                                                    BlockStatePropertyLootCondition.builder(ModBlocks.MOON_BERRY_BUSH).properties(StatePredicate.Builder.create().exactMatch(MoonBerryBush.AGE, 3))
                                            )
                                            .with(ItemEntry.builder(ModItems.MOON_BERRIES))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F)))
                                            .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
                            ).pool(LootPool.builder().conditionally(
                                            BlockStatePropertyLootCondition.builder(ModBlocks.MOON_BERRY_BUSH).properties(StatePredicate.Builder.create().exactMatch(MoonBerryBush.AGE, 2))
                                    ).with(ItemEntry.builder(ModItems.MOON_BERRIES))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                    .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE))))));

            //addDrop(ModBlocks.PINK_GARNET_BLOCK);
            //        addDrop(ModBlocks.RAW_PINK_GARNET_BLOCK);
            //        addDrop(ModBlocks.MAGIC_BLOCK);
            //
            //        addDrop(ModBlocks.PINK_GARNET_ORE, oreDrops(ModBlocks.PINK_GARNET_ORE, ModItems.RAW_PINK_GARNET));
            //        addDrop(ModBlocks.PINK_GARNET_DEEPSLATE_ORE, multipleOreDrops(ModBlocks.PINK_GARNET_DEEPSLATE_ORE, ModItems.RAW_PINK_GARNET, 3, 7));
        } catch (Exception e) {
            throw new RuntimeException("Error while running loottable datagen: " + e);
        }
    }

    //public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
    //        RegistryWrapper.Impl<Enchantment> impl = this.registryLookupFuture.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
    //        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
    //                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
    //                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    //    } //NOT WORKING!
}
