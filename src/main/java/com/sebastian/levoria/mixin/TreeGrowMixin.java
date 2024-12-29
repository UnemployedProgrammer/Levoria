package com.sebastian.levoria.mixin;

import com.sebastian.levoria.events.TreeGrowCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public class TreeGrowMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"), method = "generate", cancellable = true)
    private void grow(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfo ci) {
        ActionResult result = TreeGrowCallback.EVENT.invoker().onGrow(world, pos);

        if(result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
