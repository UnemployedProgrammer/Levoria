package com.sebastian.levoria.client_cfg;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    public static Screen createConfigScreen(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("cfg.levoria"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory client = builder.getOrCreateCategory(Text.translatable("cfg.levoria.client"));

        if(MinecraftClient.getInstance().world != null) {

            ConfigCategory server = builder.getOrCreateCategory(Text.translatable("cfg.levoria.server"));

        }

        client.addEntry(entryBuilder.startBooleanToggle(Text.translatable("cfg.levoria.client_skipExperimentalWarningsScreen"), ClientConfig.INSTANCE.isHideExperimentalWarning())
                .setDefaultValue(ClientConfig.defaultSettings().isHideExperimentalWarning()) // Recommended: Used when user click "Reset"
                .setTooltip(Text.translatable("cfg.levoria.client_skipExperimentalWarningsScreen.tooltip")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(newValue -> ClientConfig.INSTANCE.setHideExperimentalWarning(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        client.addEntry(entryBuilder.startBooleanToggle(Text.translatable("cfg.levoria.client_debugRenderPackets"), ClientConfig.INSTANCE.isAcceptDebugRendererPackets())
                .setDefaultValue(ClientConfig.defaultSettings().isAcceptDebugRendererPackets()) // Recommended: Used when user click "Reset"
                .setTooltip(Text.translatable("cfg.levoria.client_debugRenderPackets.tooltip")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(newValue -> ClientConfig.INSTANCE.setAcceptDebugRendererPackets(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        return builder.build();
    }
}
