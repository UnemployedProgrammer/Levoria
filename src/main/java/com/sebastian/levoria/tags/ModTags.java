package com.sebastian.levoria.tags;

import com.sebastian.levoria.Levoria;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Levoria.id(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REACH_ENCHANTABLE = createTag("reach_enchantable");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Levoria.id(name));
        }
    }
}
