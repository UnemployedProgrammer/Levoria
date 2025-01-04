package com.sebastian.levoria.item;

import com.mojang.serialization.Codec;
import com.sebastian.levoria.Levoria;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    //public static final ComponentType<Block> LOOKFOR_ORE =
    //            register("lookfor_ore", builder -> builder.codec(Block.CODEC.codec()));

    public static final ComponentType<Integer> LOOKFOR_ORE =
            register("lookfor_ore", builder -> builder.codec(Codec.INT));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Levoria.id(name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        Levoria.LOGGER.info("Registering Data Component Types for " + Levoria.MOD_ID);
    }
}

