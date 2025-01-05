package com.sebastian.levoria.client_cfg;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.config.ConfigEditor;
import com.sebastian.levoria.config.ConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {

    /**
     Available Keys:
     <ul>
     <li>shakeScreenTreeGrow (bool)</li>
     <li>debugMode (bool)</li>
     <li>dowsingRodBaseRange (int)</li>
     <li>dowsingRodBaseDuration (int)</li>
     </ul>
     * @param key Example: "shakeScreenTreeGrow=true,dowsingRodBaseRange=10,dowsingRodBaseDuration=160"
     */
    public static void sendServerConfig(String key, Object value) {
        try {
            MinecraftClient.getInstance().player.networkHandler.sendCommand("levoria configure \"" + key + "=" + value.toString() + "|NO-CALLBACK\"");
        } catch (Exception e) { Levoria.LOGGER.error(e.toString()); }
    }

    public static Screen createConfigScreen(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("cfg.levoria"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory client = builder.getOrCreateCategory(Text.translatable("cfg.levoria.client"));

        if(MinecraftClient.getInstance().world != null) {

            ConfigCategory server = builder.getOrCreateCategory(Text.translatable("cfg.levoria.server"));

            ConfigManager.Config d = ConfigManager.Config.defaultSettings();

            server.addEntry(entryBuilder.startBooleanToggle(Text.translatable("cfg.levoria.server_shakeScreenOnTreeGrow"), d.isShakeScreenOnTreeGrow())
                    .setDefaultValue(d.isShakeScreenOnTreeGrow()) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.translatable("cfg.levoria.server_shakeScreenOnTreeGrow.tooltip")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> sendServerConfig("shakeScreenTreeGrow", newValue)) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config

            server.addEntry(entryBuilder.startBooleanToggle(Text.translatable("cfg.levoria.server_debugMode"), d.isDebugMode())
                    .setDefaultValue(d.isShakeScreenOnTreeGrow()) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.translatable("cfg.levoria.server_debugMode.tooltip")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> sendServerConfig("debugMode", newValue)) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config

            server.addEntry(entryBuilder.startIntField(Text.translatable("cfg.levoria.server_dowsingRodBaseDuration"), d.getDowsingRodBaseDuration())
                    .setDefaultValue(d.getDowsingRodBaseDuration()) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.translatable("cfg.levoria.server_dowsingRodBaseDuration.tooltip")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> sendServerConfig("dowsingRodBaseDuration", newValue)) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config

            server.addEntry(entryBuilder.startIntSlider(Text.translatable("cfg.levoria.server_dowsingRodBaseRange"), d.getDowsingRodBaseRange(), 2, 150)
                    .setDefaultValue(d.getDowsingRodBaseRange()) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.translatable("cfg.levoria.server_dowsingRodBaseRange.tooltip")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> sendServerConfig("dowsingRodBaseRange", newValue)) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config
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
