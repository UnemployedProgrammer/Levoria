package com.sebastian.levoria.config;

import com.google.gson.Gson;
import com.sebastian.levoria.Levoria;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {

    public static Config INSTANCE = Config.defaultSettings();

    public static class Config {
        private boolean shakeScreenOnTreeGrow;
        private int dowsingRodBaseRange, dowsingRodBaseDuration;

        public Config(boolean shakeScreenOnTreeGrow, int dowsingRodBaseRange, int dowsingRodBaseDuration) {
            this.shakeScreenOnTreeGrow = shakeScreenOnTreeGrow;
            this.dowsingRodBaseRange = dowsingRodBaseRange;
            this.dowsingRodBaseDuration = dowsingRodBaseDuration;
        }

        public boolean isShakeScreenOnTreeGrow() {
            return shakeScreenOnTreeGrow;
        }

        public void setShakeScreenOnTreeGrow(boolean shakeScreenOnTreeGrow) {
            this.shakeScreenOnTreeGrow = shakeScreenOnTreeGrow;
        }

        public int getDowsingRodBaseRange() {
            return dowsingRodBaseRange;
        }

        public void setDowsingRodBaseRange(int dowsingRodBaseRange) {
            this.dowsingRodBaseRange = dowsingRodBaseRange;
        }

        public int getDowsingRodBaseDuration() {
            return dowsingRodBaseDuration;
        }

        public void setDowsingRodBaseDuration(int dowsingRodBaseDuration) {
            this.dowsingRodBaseDuration = dowsingRodBaseDuration;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Configuration: ");
            sb.append("\n  - shakeScreenOnTreeGrow: ").append(shakeScreenOnTreeGrow);
            sb.append("\n  - dowsingRodBaseRange: ").append(dowsingRodBaseRange);
            sb.append("\n  - dowsingRodBaseDuration: ").append(dowsingRodBaseDuration);
            return sb.toString();
        }

        public static Config defaultSettings() {return new Config(true, 10, 160);}

    }

    public static File CONFIG_FILE = new File("./config/levoria_server.json");

    public static Map<String, Object> toMap(Config cfg) {
        Map<String, Object> map = new HashMap<>();
        map.put("shakeScreenOnTreeGrow", cfg.shakeScreenOnTreeGrow);
        map.put("dowsingRodBaseRange", cfg.dowsingRodBaseRange);
        map.put("dowsingRodBaseDuration", cfg.dowsingRodBaseDuration);

        return map;
    }

    public static Config fromMap(Map<String, Object> cfg) {
        try {
            return new Config((Boolean) cfg.get("shakeScreenOnTreeGrow"), (Integer) cfg.get("dowsingRodBaseRange"), (Integer) cfg.get("dowsingRodBaseDuration"));
        } catch (Exception e) {
            Levoria.LOGGER.error("Error while trying to map (read-in) config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
            return Config.defaultSettings();
        }
    }

    public static void write(Config cfg) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(toMap(cfg), writer);
            Levoria.LOGGER.info("Saved config file to " + CONFIG_FILE);
        } catch (IOException e) {
            Levoria.LOGGER.error("Error while trying to save config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
        }
    }

    public static Config read() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Map<String, Object> result = gson.fromJson(reader, Map.class);
            return fromMap(result);
        } catch (IOException e) {
            Levoria.LOGGER.error("Error while trying to read config...");
            Levoria.LOGGER.error(e.getLocalizedMessage());
        }
        return Config.defaultSettings();
    }
}
