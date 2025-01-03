package com.sebastian.levoria.config;

import com.google.gson.Gson;
import com.sebastian.levoria.Levoria;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static record Config(boolean shakeScreenOnTreeGrow, int dowsingRodBaseRange, int dowsingRodBaseDuration) {}

    public static String serialize(Config cfg) {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", 1);
        map.put("key3", true);

        // Erstelle ein Gson-Objekt
        Gson gson = new Gson();

        // Konvertiere die Map in JSON
        return gson.toJson(map);
    }

    public static void write() {

    }
}
