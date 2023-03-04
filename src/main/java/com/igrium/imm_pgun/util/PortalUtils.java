package com.igrium.imm_pgun.util;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;
import com.igrium.imm_pgun.entity.IPortalEntity;
import com.igrium.imm_pgun.entity.NullPortalEntity;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/**
 * A set of static utility functions related to portals.
 */
public final class PortalUtils {
    private PortalUtils() {};

    public static void placePortal(ServerWorld world, Vec3d pos, float pitch, float yaw, PortalColor color, UUID linkID) {
        List<IPortalEntity> portals = new LinkedList<>();
        WorldUtils.collectEntitiesbyInterface(IPortalEntity.class,
                world,
                ent -> ent.getLinkID().equals(linkID) && ent.getPortalColor().equals(color),
                portals);

        portals.forEach(portal -> {
            portal.asEntity().remove(RemovalReason.KILLED);
        });

        NullPortalEntity newPortal = NullPortalEntity.TYPE.create(world);
        newPortal.setPosition(pos);
        newPortal.setPitch(pitch);
        newPortal.setYaw(yaw);
        
        newPortal.setLinkID(linkID);
        newPortal.setPortalColor(color);

        world.spawnEntity(newPortal);
    }
}
