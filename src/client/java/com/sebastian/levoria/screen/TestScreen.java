package com.sebastian.levoria.screen;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.entity.DoorMatBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class TestScreen extends Screen {
    private BlockPos pos;
    public TestScreen(BlockPos blockPos, String message) {
        super(Text.literal("Test Screen"));
        this.pos = blockPos;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        double deltaX = mouseX - (double) width / 2;
        double deltaY = mouseY - (double) height / 2;

        // Calculate the angle
        double angle = MathHelper.atan2((float) deltaY, (float) deltaX);

        BlockState blockState = ModBlocks.DOORMAT.getDefaultState();
        float scale = 100F;
        float x = 100F, y = 100F;
        float rotationX = 90F, rotationY = 0F, rotationZ = 180F;
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = client.getBlockRenderManager();

        // Extract the MatrixStack from DrawContext
        MatrixStack matrices = context.getMatrices();
        VertexConsumerProvider.Immediate vertexConsumerProvider = client.getBufferBuilders().getEntityVertexConsumers();
        BlockEntityRenderDispatcher blockEntityRenderDispatcher = client.getBlockEntityRenderDispatcher();

        // Push the current matrix stack
        matrices.push();

        // Translate and scale to position the block correctly in the GUI
        matrices.translate(x, y, 100.0F); // Set the position on screen (Z adjusts depth)
        matrices.translate(width / 2.0, height / 2.0, 100.0);
        matrices.scale(scale, scale, scale); // Scale the block as needed
        matrices.translate(-0.5, -0.5, -0.5); // Center the block
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationZ));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) angle * 20f));

        // Render the block using the BlockRenderManager
        blockRenderManager.renderBlockAsEntity(blockState, matrices, vertexConsumerProvider, 0xF000F0, OverlayTexture.DEFAULT_UV);

        matrices.translate(x, y, -100.0F);

        // Render the block entity renderer on top
        if (client.world != null && client.world.getBlockEntity(pos) instanceof DoorMatBlockEntity be) {
            BlockEntityRenderer<DoorMatBlockEntity> renderer = blockEntityRenderDispatcher.get(be);
            if (renderer != null) {
                matrices.scale(1000F, 1000F, 1000F);
                renderer.render(be, 0.0f, matrices, vertexConsumerProvider, 0xF000F0, OverlayTexture.DEFAULT_UV);
            }
        }


        // Pop the matrix stack to restore the previous state
        matrices.pop();

        // Flush the rendering to ensure the block is drawn
        vertexConsumerProvider.draw();
    }
}
