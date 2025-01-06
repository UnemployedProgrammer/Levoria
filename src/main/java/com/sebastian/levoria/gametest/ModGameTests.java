package com.sebastian.levoria.gametest;

import com.sebastian.levoria.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.state.property.Property;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class ModGameTests {
    public static void initialize() {}

    @GameTest(templateName = "minecraft:hidden_hunter", tickLimit = 200)
    public void hiddenHunter(TestContext ctx) {
        ctx.pushButton(1, 1,4);
        ctx.expectEntityAtEnd(EntityType.ARROW, 2, 1, 1);
    }

    @GameTest(templateName = "minecraft:levoria_button_stone", tickLimit = 200)
    public void btnStone(TestContext ctx) {
        ctx.pushButton(1, 2,2);
        ctx.runAtTick(10, () -> {
            ctx.expectBlockProperty(new BlockPos(1, 1, 1), RedstoneLampBlock.LIT, true);
            ctx.complete();
        });
    }

    @GameTest(templateName = "minecraft:levoria_button_bricks", tickLimit = 200)
    public void btnBricks(TestContext ctx) {
        ctx.pushButton(1, 2,2);
        ctx.runAtTick(10, () -> {
            ctx.expectBlockProperty(new BlockPos(1, 1, 1), RedstoneLampBlock.LIT, true);
            ctx.complete();
        });
    }

    @GameTest(templateName = "minecraft:levoria_door", tickLimit = 200)
    public void shadowDoor(TestContext ctx) throws InterruptedException {
        ctx.toggleLever(1, 1,2);
        ctx.runAtTick(10, () -> {
            ctx.expectBlockProperty(new BlockPos(1, 1, 1), DoorBlock.OPEN, true);
            ctx.complete();
        });
    }

    @GameTest(templateName = "minecraft:levoria_trapdoor", tickLimit = 200)
    public void shadowTrapDoor(TestContext ctx) {
        ctx.toggleLever(1, 1,2);
        ctx.runAtTick(10, () -> {
            ctx.expectBlockProperty(new BlockPos(1, 1, 1), TrapdoorBlock.OPEN, true);
            ctx.complete();
        });
    }

    @GameTest(templateName = "minecraft:levoria_pressureplate", tickLimit = 200)
    public void shadowPressurePlates(TestContext ctx) {
        ctx.spawnItem(Items.DIRT.asItem(), 1, 2, 4);
        ctx.spawnItem(Items.DIRT.asItem(), 3, 2, 4);
        ctx.runAtTick(40, () -> {
            ctx.expectBlockProperty(new BlockPos(2, 1, 0), RedstoneLampBlock.LIT, true);
            ctx.complete();
        });
    }

    @GameTest(templateName = "minecraft:levoria_treegrow", tickLimit = 200)
    public void shadowTreeGrow(TestContext ctx) {
        ctx.toggleLever(5, 2,8);
        ctx.expectBlockAtEnd(ModBlocks.SHADOW_WOOD_LOG, 5, 3, 4);
    }

    public static <T extends Comparable<T>> void finalizeWithBlockState(TestContext ctx, int x, int y, int z, Property<T> prop, T value) {
        BlockState bS = ctx.getBlockState(new BlockPos(x, y, z));
        if(bS.contains(prop)) {
            if(bS.get(prop).equals(value)) {
                ctx.complete();
            }
        }

        ctx.assertFalse(false, bS.toString() + "doesn't contain " + prop.toString());
    }
}
