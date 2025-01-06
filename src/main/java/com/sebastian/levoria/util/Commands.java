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
import com.sebastian.levoria.debug.MoonWorldPiecePlacer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class Commands {

    public static HashMap<String, MoonWorldPiecePlacer> placers = new HashMap<>();

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
                        .then(literal("configure")
                                .then(argument("changes_or_leave_empty_for_help", StringArgumentType.string())

                                        .executes(context -> {
                                            final String changes = getString(context, "changes_or_leave_empty_for_help");

                                            if(changes.isBlank()) {
                                                context.getSource().sendMessage(Text.literal("-- Help --").formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("This is a dynamic config changer, you can even set multiple configs at once.").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/levoria configure \"\""))).formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("Example: 'shakeScreenTreeGrow=false,dowsingRodBaseRange=15'").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/levoria configure \"\""))).formatted(Formatting.GREEN));
                                                context.getSource().sendMessage(Text.literal("Available Keys:").formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - shakeScreenTreeGrow       (bool)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "shakeScreenTreeGrow"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - debugMode                 (bool)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "debugMode"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - dowsingRodBaseRange       (int)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "dowsingRodBaseRange"))).formatted(Formatting.GRAY));
                                                    context.getSource().sendMessage(Text.literal("  - dowsingRodBaseDuration    (int)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "dowsingRodBaseDuration"))).formatted(Formatting.GRAY));
                                            } else {

                                                if(changes.contains("|NO-CALLBACK")) {
                                                    ConfigEditor.edit().change(changes.replace("|NO-CALLBACK", "")).save();
                                                    return 1;
                                                }

                                                ConfigEditor.edit().change(changes).save();
                                                context.getSource().sendMessage(Text.literal("Saved!").formatted(Formatting.GRAY));
                                                context.getSource().sendMessage(Text.literal("New " + ConfigManager.INSTANCE.toString()).formatted(Formatting.GRAY));
                                            }

                                            return 1;
                                        })))

                                .then(literal("fetch_cfg")

                                        .executes(context -> {
                                            context.getSource().sendMessage(Text.literal(ConfigManager.INSTANCE.toString()).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to modify..."))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/levoria configure \"\"")).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/levoria configure \"\""))).formatted(Formatting.GRAY));
                                            return 1;
                                        }))

                                .then(literal("generate_moon_world")
                                        .then(argument("name", StringArgumentType.string())
                                        .then(argument("sizeX", IntegerArgumentType.integer(1, 200))
                                        .then(argument("sizeY", IntegerArgumentType.integer(1, 200))
                                        .then(argument("sizeZ", IntegerArgumentType.integer(1, 200))

                                        .executes(context -> {
                                            try {
                                                final String name = StringArgumentType.getString(context, "name");
                                                final int sizeX = IntegerArgumentType.getInteger(context, "sizeX");
                                                final int sizeY = IntegerArgumentType.getInteger(context, "sizeY");
                                                final int sizeZ = IntegerArgumentType.getInteger(context, "sizeZ");
                                                final Vec3d pos = context.getSource().getPosition();
                                                final BlockPos bPos = new BlockPos((int) Math.round(pos.x), (int) Math.round(pos.y), (int) Math.round(pos.z));

                                                MoonWorldPiecePlacer placer = new MoonWorldPiecePlacer(context.getSource().getWorld(), bPos, sizeX, sizeY, sizeZ);
                                                placers.put(name, placer);
                                                placer.generateWorld();
                                                context.getSource().sendMessage(Text.literal("Generated at" + bPos.toShortString()));
                                            } catch (Exception e) {
                                                context.getSource().sendError(Text.literal("Error while placing: " + e.getMessage()));
                                            }
                                            return 1;
                                        }))))))
                                .then(CommandManager.literal("clear_wld")
                                        .then(CommandManager.literal("single")
                                                        .then(argument("name", StringArgumentType.string())
                                                                        .executes(context -> {
                                                                            try {
                                                                                final String name = StringArgumentType.getString(context, "name");

                                                                                MoonWorldPiecePlacer placer = placers.get(name);
                                                                                placer.clearWorld();
                                                                                placers.remove(name);
                                                                                context.getSource().sendMessage(Text.literal("Cleared " + name));
                                                                            } catch (Exception e) {
                                                                                context.getSource().sendError(Text.literal("Error while clearing: " + e.getMessage()));
                                                                            }
                                                                            return 1;
                                                                        })))
                                        .then(CommandManager.literal("all")
                                                .executes(context -> {
                                                    Iterator<Map.Entry<String, MoonWorldPiecePlacer>> iterator = placers.entrySet().iterator();
                                                    while (iterator.hasNext()) {
                                                        Map.Entry<String, MoonWorldPiecePlacer> entry = iterator.next();
                                                        try {
                                                            MoonWorldPiecePlacer placer = entry.getValue();
                                                            placer.clearWorld();
                                                            iterator.remove(); // Safely remove the entry
                                                            context.getSource().sendMessage(Text.literal("Cleared " + entry.getKey()));
                                                        } catch (Exception e) {
                                                            context.getSource().sendError(Text.literal("Error while clearing: " + e.getMessage()));
                                                        }
                                                    }
                                                    return 1;
                                                })
                                        ))

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
