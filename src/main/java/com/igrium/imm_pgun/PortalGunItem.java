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
import net.minecraft.world.World;

public class PortalGunItem extends Item {

    public PortalGunItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
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
