package com.sebastian.levoria.util;

// getString(ctx, "string")
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
// word()
import static com.mojang.brigadier.arguments.StringArgumentType.word;
// literal("foo")
import static net.minecraft.server.command.CommandManager.literal;
// argument("bar", word())
import static net.minecraft.server.command.CommandManager.argument;
// Import everything in the CommandManager
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class Commands {
        public static void registerCommands() {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(literal("shakescreen").requires((source) -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("stop"))
                        .then(argument("targets", EntityArgumentType.players())
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                            for (ServerPlayerEntity player : players) {
                                ShakeScreenEffectHandler.stopShakingPlayer(player);
                            }
                            return 1;
                        })
                        .then(CommandManager.literal("add"))
                        .then(argument("targets", EntityArgumentType.players())
                        .then(argument("duration", IntegerArgumentType.integer())
                        .then(argument("intensity", IntegerArgumentType.integer(0, 100))
                                .executes(context -> {
                                    final Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                                    final int duration = IntegerArgumentType.getInteger(context, "duration");
                                    final int intensity = IntegerArgumentType.getInteger(context, "intensity");
                                    final float intensityFloat = intensity * 0.01f;

                                    for (ServerPlayerEntity player : players) {
                                        ShakeScreenEffectHandler.shakePlayer(player, duration, intensityFloat);
                                    }

                                    return 1;
                                }))))));
            });
        }
}
