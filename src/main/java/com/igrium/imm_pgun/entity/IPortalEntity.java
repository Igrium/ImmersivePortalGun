package com.igrium.imm_pgun.entity;

import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
     * Get the direction facing out of this portal. This is only used for validity
     * checking and checking if we're on the floor or ceiling. In all other cases,
     * the entity's native rotation is used.
     * 
     * @return Portal direction.
     */
    Direction getDirection();

    /**
     * Get the position of this portal entity. Might be slightly offset from the
     * grid to combat z-fighting.
     */
    Vec3d getPos();

    /**
     * Get the world that this portal is in.
     */
    World getWorld();

    /**
     * Get the pitch of this portal entity.
     * @return Pitch in degrees.
     */
    float getPitch();

    /**
     * Get the yaw of this portal entity.
     * @return Yaw in degrees.
     */
    float getYaw();

    /**
     * Move this portal to a new location, updating any values and caches as
     * necessary. Can only be called on the server.
     * 
     * @param dimension The dimension to move into.
     * @param pos       The new position. Should be slightly offset from the grid to
     *                  combat z-fighting.
     * @param pitch     The new pitch in degrees.
     * @param yaw       The new yaw in degrees.
     * @return If the move involved spawning a new entity, the new portal entity.
     *         Otherwise, <code>this</code>.
     */
    // IPortalEntity move(ServerWorld dimension, Vec3d pos, float pitch, float yaw);

    /**
     * Whether this portal is placed on a vertical wall.
     */
    default boolean isVertical() {
        Direction direction = getDirection();
        return !(direction == Direction.DOWN || direction == Direction.UP);
    }

    /**
     * Get the Minecraft entity driving this portal.
     * @return <code>this</code> as a Minecraft entity.
     */
    Entity asEntity();
}
