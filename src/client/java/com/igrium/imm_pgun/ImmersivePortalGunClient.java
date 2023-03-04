package com.igrium.imm_pgun;

import com.igrium.imm_pgun.entity.NullPortalEntity;
import com.igrium.imm_pgun.entity.PlacedPortalEntity;
import com.igrium.imm_pgun.render.NullPortalEntityRenderer;
import com.igrium.imm_pgun.render.PlacedPortalEntityRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ImmersivePortalGunClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(NullPortalEntity.TYPE, NullPortalEntityRenderer::new);
        EntityRendererRegistry.register(PlacedPortalEntity.TYPE, PlacedPortalEntityRenderer::new);
    }
}