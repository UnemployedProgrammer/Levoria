package com.sebastian.levoria.util;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class AdvancedRegisterBlock {
    //METHODS
    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, Levoria.id(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        ModItems.registerItem(name, new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id(name))));
    }
}
