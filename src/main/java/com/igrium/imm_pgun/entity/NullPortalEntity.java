package com.igrium.imm_pgun.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
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

    public static final EntityType<NullPortalEntity> TYPE = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, NullPortalEntity::new).dimensions(EntityDimensions.fixed(1f, 2f)).build();

    private static final TrackedData<PortalColor> PORTAL_COLOR = DataTracker.registerData(
        PlacedPortalEntity.class, CustomTrackedDataHandlers.PORTAL_COLOR);

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
        if (world instanceof ServerWorld serverWorld) {
            List<NullPortalEntity> list = new ArrayList<>(1);
            serverWorld.collectEntitiesByType(NullPortalEntity.TYPE,
                    other -> other != this && other.getLinkID().equals(linkID), list, 1);

            // List will only ever have one entry.
            if (!list.isEmpty()) {
                connect(list.get(0));
                return;
            }
        }
    }

    @Override
    public float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0;
    }

    @Override
    protected Box calculateBoundingBox() {
        float width = 1;
        float height = 2;

        // Vec3d lowerCorner = new Vec3d(-width / 2, -height / 2, 0)
        //         .rotateX(Math.toRadians(getPitch())).rotateY(Math.toRadians(getYaw()));
        // Vec3d upperCorner = new Vec3d(width / 2, height / 2, 0)
        //         .rotateX(Math.toRadians(getPitch())).rotateY(Math.toRadians(getYaw()));

        // lowerCorner = lowerCorner.add(getPos());
        // upperCorner = upperCorner.add(getPos());

        // return new Box(Math.min(lowerCorner.x, upperCorner.x),
        //         Math.min(lowerCorner.y, upperCorner.y),
        //         Math.min(lowerCorner.z, upperCorner.z),
        //         Math.max(lowerCorner.x, upperCorner.x),
        //         Math.max(lowerCorner.y, upperCorner.y),
        //         Math.max(lowerCorner.z, upperCorner.z));

        Vec3d halfSize = new Vec3d(width / 2, height / 2, width / 2);
        return new Box(getPos().subtract(halfSize), getPos().add(halfSize));
    }
    

    /**
     * Connect this portal with another portal.
     * @param other The other portal.
     * @return The portal entity created to represent this portal.
     */
    protected PlacedPortalEntity connect(NullPortalEntity other) {
        if (other == this) throw new IllegalArgumentException("Can't connect to `this`.");
        if (world.isClient) throw new IllegalStateException("This method can only be called on the server.");

        PlacedPortalEntity thisPortal = PlacedPortalEntity.TYPE.create(world);
        thisPortal.setOriginPos(getPos());
        copyAttributes(this, thisPortal);

        DQuaternion thisOrientation = DQuaternion.fromEulerAngle(
                new Vec3d(thisPortal.getPitch(), thisPortal.getYaw(), 0));
        
        DQuaternion otherOrientation = DQuaternion.fromEulerAngle(
                new Vec3d(other.getPitch(), other.getYaw(), 0));

        // Subtract this orientation from the other to get the delta.
        DQuaternion deltaOrientation = otherOrientation.combine(thisOrientation.getConjugated()).combine(
                DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), 180));

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
