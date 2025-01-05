package com.sebastian.levoria.util;

import com.sebastian.levoria.Levoria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static SoundEvent MOON_ATMOS = registerSoundEvent("moon_atmos");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Levoria.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Levoria.LOGGER.info("Registering Mod Sounds for " + Levoria.MOD_ID);
    }
}
