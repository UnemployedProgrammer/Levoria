package com.sebastian.levoria;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.entity.ModBlockEntities;
import com.sebastian.levoria.block_entity_renderer.HiddenHunterBlockEntityRenderer;
import com.sebastian.levoria.effects.MoonDimensionEffects;
import com.sebastian.levoria.effects.ScreenShakeEffect;
import com.sebastian.levoria.network.HighlightBlockS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import com.sebastian.levoria.util.ShakeScreenEffectHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LevoriaClient implements ClientModInitializer {

	public static Boolean IN_RENDERING = false;

	private static class BlockHighlightInstance {

		private final BlockPos pos;
		private final BlockState state;
		private Integer ticks;

		public BlockHighlightInstance(BlockPos pos, Integer ticks, BlockState state) {
			this.pos = pos;
			this.ticks = ticks;
			this.state = state;
		}

		public BlockPos getPos() {
			return pos;
		}

		public BlockState getState() {
			return state;
		}

		private void countDown() {
			ticks -= 1;
		}
	}

	List<BlockHighlightInstance> highlightedBlocks = new ArrayList<>();

	public static void applyDimensionEffect(DimensionEffects effects, Identifier id) {
		try {
			// Access the private BY_IDENTIFIER field in DimensionEffects
			Field byIdentifierField = DimensionEffects.class.getDeclaredField("BY_IDENTIFIER");
			byIdentifierField.setAccessible(true);

			// Get the map instance
			@SuppressWarnings("unchecked")
			Map<Identifier, DimensionEffects> effectsMap =
					(Map<Identifier, DimensionEffects>) byIdentifierField.get(null);

			// Add your custom dimension effects
			effectsMap.put(id, effects);

		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onInitializeClient() {
		ScreenShakeEffect.INSTANCE = new ScreenShakeEffect();
		BlockEntityRendererRegistry.register(ModBlockEntities.HIDDEN_HUNTER, HiddenHunterBlockEntityRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MOON_BERRY_BUSH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADOW_WOOD_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADOW_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADOW_TRAPDOOR, RenderLayer.getCutout());
		applyDimensionEffect(new MoonDimensionEffects(), Levoria.getId("moon"));

		ClientPlayNetworking.registerGlobalReceiver(TotemAnimationS2C.ID, (payload, context) -> {
			context.client().execute(() -> {
				MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(TotemAnimationS2C.toBlock(payload.block())));
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(HighlightBlockS2C.ID, (payload, context) -> {
			context.client().execute(() -> {
				highlightedBlocks.add(new BlockHighlightInstance(payload.block(), 160, context.client().world.getBlockState(payload.block())));
				Levoria.LOGGER.info("Going to highlight block at " + payload.block().toShortString());
				ScreenShakeEffect.INSTANCE.shakeScreen(10, 0.1f);
			});
		});

		/////////////////////////////////////////////////////////////////// RENDERING ////////////////////////////////////////////////////////////////////////

		WorldRenderEvents.END.register((ctx) -> {
			IN_RENDERING = true;
			for (BlockHighlightInstance highlightedBlock : highlightedBlocks) {
				renderBlock(ctx, highlightedBlock.getState(), highlightedBlock.getPos());
			}
			IN_RENDERING = false;
		});

		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			Iterator<BlockHighlightInstance> iterator = highlightedBlocks.iterator();
			while (iterator.hasNext()) {
				BlockHighlightInstance highlightedBlock = iterator.next();
				highlightedBlock.countDown();
				if (highlightedBlock.ticks <= 0) {
					iterator.remove(); // Safe removal
				}
			}

			ScreenShakeEffect.INSTANCE.updateShake();
		});
	}

	private static void renderBlock(WorldRenderContext context, BlockState state, BlockPos posM) {
		MinecraftClient client = MinecraftClient.getInstance();
		VertexConsumerProvider providers = context.consumers();

		//System.out.println(providers.toString() + " | " + client.world.toString());
		if (providers != null && client.world != null) {

			BlockPos pos = posM;
			VertexConsumer consumer = providers.getBuffer(RenderLayers.getBlockLayer(state));

			Vec3d camPos = context.camera().getPos();

			MatrixStack matrices = context.matrixStack();
			if (matrices == null)
				return;

			matrices.push();
			matrices.translate(-camPos.x, -camPos.y, -camPos.z);
			matrices.translate(pos.getX(), pos.getY(), pos.getZ());
			//client.getBlockRenderManager().renderBlock(state, pos, client.world, matrices, consumer, false, client.world.getRandom());
			renderBlockWithLight(state, pos, client.world, matrices, consumer, false, client.world.getRandom());
			matrices.pop();
		}
	}

	public static void renderBlockWithLight(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random) {
		int light = 0xF000F0; //Light level 15
		try {
			MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(world, MinecraftClient.getInstance().getBlockRenderManager().getModel(state), state, pos, matrices, vertexConsumer, cull, random, light, OverlayTexture.DEFAULT_UV);
		} catch (Throwable v) {
			Throwable throwable = v;
			CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world (FAKE BLOCK RENDERING)");
			CrashReportSection crashReportSection = crashReport.addElement("Block b. tesselated!");
			CrashReportSection.addBlockInfo(crashReportSection, world, pos, state);
			throw new CrashException(crashReport);
		}
	}
}