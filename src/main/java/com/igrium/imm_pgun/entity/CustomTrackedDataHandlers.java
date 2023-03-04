package com.igrium.imm_pgun.entity;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class CustomTrackedDataHandlers {
    public static final TrackedDataHandler<PortalColor> PORTAL_COLOR = TrackedDataHandler.ofEnum(PortalColor.class);

    static {
        TrackedDataHandlerRegistry.register(PORTAL_COLOR);
    }
}
