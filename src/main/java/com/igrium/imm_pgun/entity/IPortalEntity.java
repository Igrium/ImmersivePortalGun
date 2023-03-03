package com.igrium.imm_pgun.entity;

import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;

import net.minecraft.entity.Entity;

/**
 * An entity that resembles a placed portal. Usually a {@code PlacedPortalEntity} or a {@code NullPortalEntity}
 */
public interface IPortalEntity {

    /**
     * Get the color of this portal.
     */
    PortalColor getPortalColor();

    /**
     * Get the "link ID" of this portal. Only two portals can exist with a given
     * link ID, and they will link with each other.
     * 
     * @return The link ID as a UUID.
     */
    UUID getLinkID();

    /**
     * Get the Minecraft entity driving this portal.
     * @return <code>this</code> as a Minecraft entity.
     */
    Entity asEntity();
}
