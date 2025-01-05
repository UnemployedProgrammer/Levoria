package com.sebastian.levoria.config;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.network.HighlightBlockS2C;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public class ConfigEditor {
    private final ConfigManager.Config CONFIG;

    private ConfigEditor(ConfigManager.Config config) {
        this.CONFIG = config;
    }

    public static ConfigEditor edit() {
        return new ConfigEditor(ConfigManager.INSTANCE);
    }

    /**
     * @param changeStr Example: "shakeScreenTreeGrow=true,dowsingRodBaseRange=10,dowsingRodBaseDuration=160"
     Available Keys:
     - shakeScreenTreeGrow (bool)
     - debugMode (bool)
     - dowsingRodBaseRange (int)
     - dowsingRodBaseDuration (int)
     */
    public ConfigEditor change(String changeStr) {
        String[] pairs = changeStr.split(",");

        // Loop through each pair and split by the '=' symbol
        for (String pair : pairs) {
            //System.out.println(pair);
            String[] keyValue = pair.split("=");

            // Ensure there are exactly two parts (key and value)
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                // Print the result in the desired format
                change(key, value);
            }
        }
        return this;
    }

    public void save() {
        ConfigManager.INSTANCE = CONFIG;
    }

    /**
    Available Keys:
        - shakeScreenTreeGrow (bool)
        - debugMode (bool)
        - dowsingRodBaseRange (int)
        - dowsingRodBaseDuration (int)
     */
    public ConfigEditor change(String cfg, String newVal) {

        //System.out.println("Change: " + cfg + "|" + newVal);

        try {
            switch (cfg) {
                case "shakeScreenTreeGrow": CONFIG.setShakeScreenOnTreeGrow(Boolean.parseBoolean(newVal)); break;
                case "debugMode": CONFIG.setDebugMode(Boolean.parseBoolean(newVal)); break;
                case "dowsingRodBaseRange": CONFIG.setDowsingRodBaseRange(Integer.parseInt(newVal)); break;
                case "dowsingRodBaseDuration": CONFIG.setDowsingRodBaseDuration(Integer.parseInt(newVal)); break;
                case null, default: Levoria.LOGGER.warn("Invalid CONFIG key in editor: " + cfg); break;
            }
        } catch (Exception e) {
            Levoria.LOGGER.warn("(Config Editor) Tried to parse or save: " + newVal + " to entry/key " + cfg + ". Please contact us if this happens often.");
        }

        return this;
    }

    /**
     Available Keys:
     <ul>
     <li>shakeScreenTreeGrow (bool)</li>
     <li>debugMode (bool)</li>
     <li>dowsingRodBaseRange (int)</li>
     <li>dowsingRodBaseDuration (int)</li>
     </ul>
     * @param resolvableMessage Example: "shakeScreenTreeGrow=true,dowsingRodBaseRange=10,dowsingRodBaseDuration=160"
     */
    public static record ConfigurationPayload(String resolvableMessage) implements CustomPayload {
        public static final Id<ConfigurationPayload> ID = new Id<>(Levoria.id("configure_server"));
        public static final PacketCodec<RegistryByteBuf, ConfigurationPayload> PACKET_CODEC =
                PacketCodec.tuple(PacketCodecs.STRING, ConfigurationPayload::resolvableMessage, ConfigurationPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

}
