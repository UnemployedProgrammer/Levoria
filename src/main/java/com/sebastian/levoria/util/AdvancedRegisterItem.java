package com.sebastian.levoria.util;

import com.sebastian.levoria.Levoria;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AdvancedRegisterItem {
    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, Levoria.id(name), item);
    }
}
