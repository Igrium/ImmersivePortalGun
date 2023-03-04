package com.igrium.imm_pgun.test;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.UUID;

import com.igrium.imm_pgun.ImmersivePortalGunMod.PortalColor;
import com.igrium.imm_pgun.util.PortalUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

public final class SpawnPortalCommand {

    private static final UUID DEFAULT_LINK_ID = UUID.fromString("3bfc2984-6b13-42a7-a948-648779a94d89");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
            RegistrationEnvironment environment) {
        
        // dispatcher.register(literal("spawnPortal").then(
        //     argument("position", Vec3ArgumentType.vec3(true)).then(
        //         argument("linkID", UuidArgumentType.uuid()).executes(context -> {
        //             return spawnPortal(context, UuidArgumentType.getUuid(context, "linkID"));
        //         })
        //     ).executes(context -> {
        //         return spawnPortal(context, DEFAULT_LINK_ID);
        //     })
        // ));

        dispatcher.register(literal("spawnPortal").then(
            argument("position", Vec3ArgumentType.vec3(true)).then(
                argument("color", PortalColorArgumentType.portalColor()).then(
                    argument("linkID", UuidArgumentType.uuid()).executes(context -> {
                        return spawnPortal(context, UuidArgumentType.getUuid(context, "linkID"));
                    })
                ).executes(context -> {
                    return spawnPortal(context, DEFAULT_LINK_ID);
                })
            )
        ));
    }

    private static Random random = new LocalRandom(0);

    private static int spawnPortal(CommandContext<ServerCommandSource> context, UUID linkID) {
        try {
            Vec3d pos = Vec3ArgumentType.getVec3(context, "position");
            PortalColor color = PortalColorArgumentType.getPortalColor(context, "color");

            PortalUtils.placePortal(context.getSource().getWorld(), pos, 0, MathHelper.nextFloat(random, -180, 180), color, linkID);
    
            Text uuidText = Text.literal(linkID.toString()).setStyle(
                Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, linkID.toString())));
    
            context.getSource().sendFeedback(Text.literal("Spawned new null portal with linkID: ").append(uuidText), true);
    
            return 1;
        } catch (Throwable e) {
            LogUtils.getLogger().error("Error spawning portal", e);
            throw e;
        }
    }
    
}
