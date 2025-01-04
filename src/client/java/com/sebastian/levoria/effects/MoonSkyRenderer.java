package com.sebastian.levoria.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sebastian.levoria.Levoria;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class MoonSkyRenderer {

    private static final Identifier EARTH_TEXTURE = Levoria.id("/textures/atmosphere/earth.png");

    public static void renderEarth(float alpha, Tessellator tesselator, MatrixStack matrices) {
        float size = 20.0F; // Moon size
        float z = -100.0F; // Z depth

        // Set up rendering
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, EARTH_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);

        // Prepare vertices for the custom moon quad
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        bufferBuilder.vertex(matrix, -size, z, size).texture(1.0F, 1.0F);
        bufferBuilder.vertex(matrix, size, z, size).texture(0.0F, 1.0F);
        bufferBuilder.vertex(matrix, size, z, -size).texture(0.0F, 0.0F);
        bufferBuilder.vertex(matrix, -size, z, -size).texture(1.0F, 0.0F);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        // Restore rendering settings
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }



}
