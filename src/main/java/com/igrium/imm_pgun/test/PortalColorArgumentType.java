package com.igrium.imm_pgun.test;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class PortalColorArgumentType extends EnumArgumentType<PortalColor> {

    protected PortalColorArgumentType() {
        super(PortalColor.CODEC, PortalColor::values);
    }
    
    public static PortalColorArgumentType portalColor() {
        return new PortalColorArgumentType();
    }

    public static PortalColor getPortalColor(CommandContext<?> context, String id) {
        return context.getArgument(id, PortalColor.class);
    }

    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(new Identifier("immersive_portalgun", "portal_color"),
                PortalColorArgumentType.class, ConstantArgumentSerializer.of(PortalColorArgumentType::portalColor));
    }
}
