package com.igrium.imm_pgun.test;

import java.util.UUID;

import com.igrium.imm_pgun.entity.NullPortalEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.*;

public final class SpawnPortalCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
            RegistrationEnvironment environment) {
        
        dispatcher.register(literal("spawnPortal").then(
            argument("position", Vec3ArgumentType.vec3(true)).then(
                argument("linkID", UuidArgumentType.uuid()).executes(context -> {
                    return spawnPortal(context, UuidArgumentType.getUuid(context, "linkID"));
                })
            ).executes(context -> {
                return spawnPortal(context, UUID.randomUUID());
            })
        ));
    }

    private static Random random = new LocalRandom(0);

    private static int spawnPortal(CommandContext<ServerCommandSource> context, UUID linkID) {
        try {
            Vec3d pos = Vec3ArgumentType.getVec3(context, "position");
            NullPortalEntity entity = NullPortalEntity.TYPE.create(context.getSource().getWorld());
    
            entity.setPosition(pos);
            entity.setYaw(MathHelper.nextFloat(random, -180, 180));
            entity.setLinkID(linkID);
    
            entity.getWorld().spawnEntity(entity);
    
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
