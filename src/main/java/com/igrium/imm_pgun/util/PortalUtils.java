package com.igrium.imm_pgun.util;

import java.util.Objects;

import qouteall.imm_ptl.core.portal.Portal;

/**
 * A set of static utility functions related to portals.
 */
public final class PortalUtils {
    private PortalUtils() {};

    /**
     * Link two existing portal entities. The position and rotation of each portal
     * entity is maintained, and all the other attributes of the first portal entity
     * are copied to the second.
     * 
     * @param first  The first portal.
     * @param second The second portal.
     * @param sync   Whether to sync this change to the client.
     */
    public static void linkPortals(Portal first, Portal second, boolean sync) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        first.dimensionTo = second.getOriginDim();
        first.setDestination(second.getOriginPos());

        if (second.getRotation() != null) {
            
        }
    }
}
