package com.sebastian.levoria.enchantment;

import com.mojang.serialization.MapCodec;
import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.enchantment.custom.DowsingRodReachEnchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> REACH =
            registerEntityEffect("reach", DowsingRodReachEnchantment.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Levoria.id(name), codec);
    }

    public static void registerEnchantmentEffects() {
        Levoria.LOGGER.info("Registering Mod Enchantment Effects for " + Levoria.MOD_ID);
    }
}
