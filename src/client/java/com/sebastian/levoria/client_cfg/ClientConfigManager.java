package com.sebastian.levoria.client_cfg;

import com.google.gson.Gson;
import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.config.ConfigManager;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientConfigManager {
    public static File CONFIG_FILE = new File("config/levoria.json");

    public static Map<String, Object> toMap(ClientConfig cfg) {
        Map<String, Object> map = new HashMap<>();
        map.put("isHideExperimentalWarning", cfg.isHideExperimentalWarning());
        map.put("getHideExperimentalWarningDelay", cfg.getHideExperimentalWarningDelay());

        return map;
    }

    public static ClientConfig fromMap(Map<String, Object> cfg) {
        try {
            return new ClientConfig((Boolean) cfg.get("isHideExperimentalWarning"), (Double) cfg.get("getHideExperimentalWarningDelay"));
        } catch (Exception e) {
            Levoria.LOGGER.error("Error while trying to map (read-in) config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
            return ClientConfig.defaultSettings();
        }
    }

    public static void write(ClientConfig cfg) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(toMap(cfg), writer);
            Levoria.LOGGER.info("Saved client config file to " + CONFIG_FILE);
        } catch (IOException e) {
            Levoria.LOGGER.error("Error while trying to save config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
        }
    }

    public static ClientConfig read() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Map<String, Object> result = gson.fromJson(reader, Map.class);
            return fromMap(result);
        } catch (IOException e) {
            Levoria.LOGGER.error("Error while trying to read config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
        }
        return ClientConfig.defaultSettings();
    }

    public static void registerConfigUn_Loaders() {
        ServerWorldEvents.UNLOAD.register(Event.DEFAULT_PHASE, (world, serverWorld) -> {
            write(ClientConfig.INSTANCE);
        });
    }
}
