package com.sebastian.levoria.block_entity_renderer;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.DoorMatBlock;
import com.sebastian.levoria.block.entity.DoorMatBlockEntity;
import com.sebastian.levoria.block.entity.HiddenHunterBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Font;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DoorMatBlockEntityRenderer implements BlockEntityRenderer<DoorMatBlockEntity> {

    private final BlockEntityRendererFactory.Context ctx;

    public DoorMatBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void render(DoorMatBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        float DEFAULT_SCALE = 0.006f;
        Direction facing = entity.getWorld().getBlockState(entity.getPos()).get(DoorMatBlock.FACING, Direction.NORTH);

        // Center the text on the block
        matrices.translate(0.5, 0.07, 0.5); // Slightly above the ground
        // Rotate based on facing direction
        float rotation = switch (facing) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> -90;
            default -> 0;
        };

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));

        // Scale the text dynamically
        float scale = DEFAULT_SCALE * (20.0f / getText(entity.getMessage()).getString().length()); // Adjust scale based on text length
        matrices.scale(scale, scale, scale);


        ctx.getTextRenderer().draw(getText(entity.getMessage()), -MinecraftClient.getInstance().textRenderer.getWidth(getText(entity.getMessage())) / 2f, -MinecraftClient.getInstance().textRenderer.fontHeight / 2f, 0xECECEC, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0x000000, light);

        matrices.pop();

    }

    public static Text getText(String message) {
        return message.contains("translate->") ? Text.translatable(message.replace("translate->", "")) : Text.literal(message);
    }

    @Override
    public int getRenderDistance() {
        return 20;
    }

    /*
    float rotation = switch (FACING) {
            case NORTH: yield 180F;
            case EAST: yield 90F;
            case WEST: yield 270F;
            case null, default: yield 0F;
        }; //SOUTH: 0F (Using default)
        matrices.scale(0.02f * scale, 0.02f * scale, 0.02f * scale);
        matrices.translate(10f, 3.5f, 20f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));


        public int draw(     String text,
        float x,
        float y,
        int color,
        boolean shadow,
        Matrix4f matrix,
        net. minecraft. client. render. VertexConsumerProvider vertexConsumers,
        TextRenderer. TextLayerType layerType,
        int backgroundColor,
        int light )
     */

}
