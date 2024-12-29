package com.sebastian.levoria.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

/**
 * Callback for growing a tree.
 * Called before the sheep is sheared, items are dropped, and items are damaged.
 * Upon return:
 * - SUCCESS cancels further processing and continues with normal growing behavior.
 * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
 * - FAIL cancels further processing and does not grow the tree/sapling.
 */
public interface TreeGrowCallback {

    Event<TreeGrowCallback> EVENT = EventFactory.createArrayBacked(TreeGrowCallback.class,
            (listeners) -> (world, pos) -> {
                for (TreeGrowCallback listener : listeners) {
                    ActionResult result = listener.onGrow(world, pos);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult onGrow(ServerWorld player, BlockPos sheep);
}
