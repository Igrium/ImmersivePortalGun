package com.igrium.imm_pgun;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.ExplosionBehavior;

public class PortalGunItem extends Item {

    private static final double MAX_RAYCAST_DISTANCE = 128d;

    public PortalGunItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.consume(user.getStackInHand(hand));
        
        HitResult hit = user.raycast(MAX_RAYCAST_DISTANCE, 1, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            world.createExplosion(user, null, new ExplosionBehavior(), hit.getPos().getX(), hit.getPos().getY(), hit.getPos().getZ(), 1, false, ExplosionSourceType.TNT);
        }

        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    public UUID getOrCreateLinkID(ItemStack stack) {
        Optional<UUID> opt = getLinkID(stack);
        if (opt.isPresent()) return opt.get();
        else {
            UUID uuid = UUID.randomUUID();
            setLinkID(stack, uuid);
            return uuid;
        }
    }

    public Optional<UUID> getLinkID(ItemStack stack) {
        if (!stack.hasNbt()) return Optional.empty();
        if (!stack.getNbt().contains("linkID")) return Optional.empty();
        stack.getName();

        return Optional.of(stack.getNbt().getUuid("linkID"));
    }

    public void setLinkID(ItemStack stack, @Nullable UUID linkID) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (linkID != null) {
            nbt.putUuid("linkID", linkID);
        } else {
            nbt.remove("linkID");
        }
    }

    public void setLinkID(ItemStack stack, Optional<UUID> linkID) {
        setLinkID(stack, linkID.orElse(null));
    }
}
