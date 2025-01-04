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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sebastian.levoria.config.ConfigEditor;
import com.sebastian.levoria.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.List;

public class Commands {
        public static void registerCommands() {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(literal("shakescreen").requires((source) -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("stop")

                                .then(argument("targets", EntityArgumentType.players())

                                        .executes(context -> {
                                            final Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                                                for (ServerPlayerEntity player : players) {
                                                    ShakeScreenEffectHandler.stopShakingPlayer(player);
                                                }
                                                return 1;
                                        })))

                                .then(CommandManager.literal("add")
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

                dispatcher.register(literal("levoria").requires((source) -> source.hasPermissionLevel(4))
                        .then(CommandManager.literal("configure")
                                .then(argument("changes_or_leave_empty_for_help", StringArgumentType.string())

                                        .executes(context -> {
                                            final String changes = StringArgumentType.getString(context, "changes_or_leave_empty_for_help");

                                            if(changes.isBlank()) {
                                                context.getSource().sendMessage(Text.literal("-- Help --").formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("This is a dynamic config changer, you can even set multiple configs at once.").formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("Example: 'shakeScreenTreeGrow=false,dowsingRodBaseRange=15'").formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("Available Keys:").formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - shakeScreenTreeGrow       (bool)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "shakeScreenTreeGrow"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - debugMode                 (bool)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "debugMode"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - dowsingRodBaseRange       (int)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "dowsingRodBaseRange"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - dowsingRodBaseDuration    (int)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "dowsingRodBaseDuration"))).formatted(Formatting.GRAY));
                                            } else {
                                                ConfigEditor.edit().change(changes).save();
                                                context.getSource().sendMessage(Text.literal("Saved!").formatted(Formatting.GRAY));
                                                context.getSource().sendMessage(Text.literal("New " + ConfigManager.INSTANCE.toString()).formatted(Formatting.GRAY));
                                            }

                                            return 1;
                                        })))

                        /*
                        .then(CommandManager.literal("add")
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
                                                        })))))*/);


            });
        }

    /*
     * @param changeStr Example: "shakeScreenTreeGrow=true,dowsingRodBaseRange=10,dowsingRodBaseDuration=160"
    Available Keys:
    - shakeScreenTreeGrow (bool)
    - dowsingRodBaseRange (int)
    - dowsingRodBaseDuration (int)
     */
}
