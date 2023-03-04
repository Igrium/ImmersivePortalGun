package com.igrium.imm_pgun.render;

import com.igrium.imm_pgun.entity.NullPortalEntity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class NullPortalEntityRenderer extends EntityRenderer<NullPortalEntity> {

    public NullPortalEntityRenderer(Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(NullPortalEntity entity) {
        return null;
    }
    
}
