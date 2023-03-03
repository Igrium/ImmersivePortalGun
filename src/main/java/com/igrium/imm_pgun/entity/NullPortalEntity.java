package com.igrium.imm_pgun.entity;

import java.util.Objects;
import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.q_misc_util.my_util.DQuaternion;

/**
 * A portal which has been placed but does not yet connect to another portal.
 */
public class NullPortalEntity extends Entity implements IPortalEntity {

    public static final EntityType<NullPortalEntity> TYPE = FabricEntityTypeBuilder.create(SpawnGroup.MISC, NullPortalEntity::new).build();

    private static final TrackedData<PortalColor> PORTAL_COLOR = DataTracker.registerData(PlacedPortalEntity.class,
            TrackedDataHandler.ofEnum(PortalColor.class));

    private UUID linkID = UUID.randomUUID();
    private Direction direction;
    
    public NullPortalEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(PORTAL_COLOR, PortalColor.BLUE);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("portalColor", NbtElement.BYTE_TYPE)) {
            int ordinal = Byte.toUnsignedInt(nbt.getByte("portalColor"));
            setPortalColor(PortalColor.values()[ordinal]);
        }
        if (nbt.contains("linkID", NbtElement.INT_ARRAY_TYPE)) {
            setLinkID(nbt.getUuid("linkID"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putByte("portalColor", (byte) getPortalColor().ordinal());
        nbt.putUuid("linkID", linkID);
    }
    
    @Override
    public PortalColor getPortalColor() {
        return dataTracker.get(PORTAL_COLOR);
    }

    protected void setPortalColor(PortalColor portalColor) {
        dataTracker.set(PORTAL_COLOR, portalColor);
    }

    @Override
    public UUID getLinkID() {
        return linkID;
    }

    public void setLinkID(UUID linkID) {
        Objects.requireNonNull(linkID);
        this.linkID = linkID;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public NullPortalEntity asEntity() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();

        // Because we remove these entities during the tick cycle, we need to make sure
        // this wasn't removed by another instance.
        if (isRemoved()) return;
        if (world.isClient) return;


    }

    /**
     * Connect this portal with another portal.
     * @param other The other portal.
     * @return The portal entity created to represent this portal.
     */
    protected PlacedPortalEntity connect(NullPortalEntity other) {
        if (world.isClient) throw new IllegalStateException("This method can only be called on the server.");

        PlacedPortalEntity thisPortal = PlacedPortalEntity.TYPE.create(world);
        thisPortal.setOriginPos(getPos());
        copyAttributes(this, thisPortal);

        DQuaternion thisOrientation = DQuaternion.fromEulerAngle(
                new Vec3d(thisPortal.getYaw(), thisPortal.getPitch(), 0));
        
        DQuaternion otherOrientation = DQuaternion.fromEulerAngle(
                new Vec3d(other.getYaw(), other.getPitch(), 0));

        // Subtract this orientation from the other to get the delta.
        DQuaternion deltaOrientation = otherOrientation.combine(thisOrientation.getConjugated());

        thisPortal.setDestination(other.getPos());
        thisPortal.setDestinationDimension(other.world.getRegistryKey());
        thisPortal.setOrientationAndSize(
                McHelper.getAxisWFromOrientation(thisOrientation),
                McHelper.getAxisHFromOrientation(thisOrientation),
                1, 2);
        thisPortal.setRotation(deltaOrientation);

        PlacedPortalEntity otherPortal = PortalAPI.createReversePortal(thisPortal);
        copyAttributes(other, otherPortal); // createReversePortal sets up the position.

        thisPortal.world.spawnEntity(thisPortal);
        otherPortal.world.spawnEntity(otherPortal);

        this.remove(RemovalReason.KILLED);
        other.remove(RemovalReason.KILLED);

        return thisPortal;
    }

    private static void copyAttributes(NullPortalEntity from, PlacedPortalEntity to) {
        to.setPitch(from.getPitch());
        to.setYaw(from.getYaw());
        to.setDirection(from.getDirection());

        to.setPortalColor(from.getPortalColor());
        to.setLinkID(from.getLinkID());
    }

    // private static void initOrientationAndSize(Portal portal, float pitch, float yaw) {
    //     DQuaternion orientation = DQuaternion.fromEulerAngle(new Vec3d(yaw, pitch, 0));
    //     portal.setOrientationRotation(orientation);
    //     portal.setWidth(1);
    //     portal.setHeight(2);
    // }
}
