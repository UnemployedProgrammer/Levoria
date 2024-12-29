package com.sebastian.levoria.world.tree;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.events.TreeGrowCallback;
import com.sebastian.levoria.util.RadiusHelper;
import com.sebastian.levoria.util.ShakeScreenEffectHandler;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ShadowTreeEffects {

    public static void onGrow(ServerWorld world, BlockPos pos) {
        for (ServerPlayerEntity playerInRadius : RadiusHelper.getPlayersInRadius(world, pos, 10)) {
            ShakeScreenEffectHandler.shakePlayer(playerInRadius, 10, 0.1f);
        }
    }

    public static void registerIt() {
        TreeGrowCallback.EVENT.register((world, pos) -> {
            if(world.getBlockState(pos).isOf(ModBlocks.SHADOW_WOOD_LOG) && world.getBlockState(pos.up()).isOf(ModBlocks.SHADOW_WOOD_LOG)) { //Should be a Tree!
                onGrow(world, pos);
            }
            return ActionResult.PASS;
        });
    }

}
