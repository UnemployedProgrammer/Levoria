package com.sebastian.levoria.debug_renderers;

import com.sebastian.levoria.network.DebugRenderingS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DebugRendererRouter {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(DebugRenderingS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                route(payload.action(), List.of(payload.param1(), payload.param2(), payload.param3(), payload.param4()));
            });
        });
    }

    public static void render(WorldRenderContext ctx) {
        RangeDebugRenderer.render(ctx);
    }

    public static void route(String action, List<String> parameters) {
        if(action.equals("radius")) {
            BlockPos around = new BlockPos((int) Math.round(Double.parseDouble(parameters.get(0))), (int) Math.round(Double.parseDouble(parameters.get(1))), (int) Math.round(Double.parseDouble(parameters.get(2))));
            Integer radius = Integer.parseInt(parameters.get(3));
            RangeDebugRenderer.shouldBegin(around, radius);
        }
        if(action.equals("c_radius")) { //Cancel-Radius
            RangeDebugRenderer.shouldStop();
        }
    }
}
