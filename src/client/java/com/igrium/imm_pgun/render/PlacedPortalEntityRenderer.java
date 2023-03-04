package com.igrium.imm_pgun.render;

import org.joml.Quaternionf;

import com.igrium.imm_pgun.entity.PlacedPortalEntity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import qouteall.imm_ptl.core.IPCGlobal;

public class PlacedPortalEntityRenderer extends EntityRenderer<PlacedPortalEntity> {

    public PlacedPortalEntityRenderer(Context ctx) {
        super(ctx);
    }

    private static final Identifier TEXTURE = new Identifier("immersive_portalgun", "textures/entity/portal/portalframe.png");

    @Override
    public void render(PlacedPortalEntity entity, float yaw, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        IPCGlobal.renderer.renderPortalInEntityRenderer(entity);

        matrices.push();
        matrices.multiply(new Quaternionf().rotateY((float) Math.toRadians(-yaw)));
        matrices.translate(0, 0, .01);
        
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(TEXTURE));
        RenderUtils.drawQuad(matrices.peek(), consumer, -.5f, -1f, .5f, 1f, entity.getPortalColor().getColor());

        matrices.pop();
    }

    @Override
    public Identifier getTexture(PlacedPortalEntity var1) {
        return TEXTURE;
    }
    
}
