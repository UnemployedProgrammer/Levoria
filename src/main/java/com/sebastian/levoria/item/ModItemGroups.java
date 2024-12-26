package com.sebastian.levoria.item;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModItemGroups {
    public static final List<ItemConvertible> preRegister = new ArrayList<>();
    public static ItemGroup FLUORITE_GROUP;

    public static <T extends ItemConvertible> T add(T entry) {
        if (preRegister.contains(entry)) return entry;
        preRegister.add(entry);
        return entry;
    }

    public static void registerModItemGrops() {
        FLUORITE_GROUP = Registry.register(Registries.ITEM_GROUP,
                Identifier.of(Levoria.MOD_ID, "levoria"),
                FabricItemGroup.builder().displayName(Text.translatable("itemgroup.levoria"))
                        .icon(() -> new ItemStack(ModBlocks.MOON_STONE.asItem())).entries((displayContext, entries) -> {

                            for (ItemConvertible itemConvertible : preRegister) {
                                entries.add(itemConvertible);
                            }

                            //entries.add(ModBlocks.MOON_STONE.asItem());
                            //                            entries.add(ModBlocks.MOON_STONE_STAIRS.asItem());
                            //                            entries.add(ModBlocks.MOON_STONE_SLAB.asItem());
                            //                            entries.add(ModBlocks.MOON_STONE_PRESSURE_PLATE.asItem());
                            //                            entries.add(ModBlocks.MOON_STONE_BUTTON.asItem());
                            //                            entries.add(ModBlocks.MOON_STONE_WALL.asItem());
                            //
                            //                            entries.add(ModBlocks.HIDDEN_HUNTER.asItem());
                            //
                            //                            entries.add(ModItems.DOWSING_ROD);

                        }).build());
    }
}
