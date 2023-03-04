package com.igrium.imm_pgun.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {
    public static void drawQuad(MatrixStack.Entry matrix, VertexConsumer buffer, float minX, float minY, float maxX, float maxY, int color) {
        Matrix4f position = matrix.getPositionMatrix();
        Matrix3f normal = matrix.getNormalMatrix();

        buffer.vertex(position, maxX, minY, 0);
        buffer.color(color);
        buffer.texture(1, 1);
        buffer.overlay(OverlayTexture.DEFAULT_UV);
        buffer.light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
        buffer.normal(normal, 0, 0, 1);
        buffer.next();

        buffer.vertex(position, maxX, maxY, 0);
        buffer.color(color);
        buffer.texture(1, 0);
        buffer.overlay(OverlayTexture.DEFAULT_UV);
        buffer.light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
        buffer.normal(normal, 0, 0, 1);
        buffer.next();

        buffer.vertex(position, minX, maxY, 0);
        buffer.color(color);
        buffer.texture(0, 0);
        buffer.overlay(OverlayTexture.DEFAULT_UV);
        buffer.light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
        buffer.normal(normal, 0, 0, 1);
        buffer.next();

        buffer.vertex(position, minX, minY, 0);
        buffer.color(color);
        buffer.texture(0, 1);
        buffer.overlay(OverlayTexture.DEFAULT_UV);
        buffer.light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
        buffer.normal(normal, 0, 0, 1);
        buffer.next();
    }
}
