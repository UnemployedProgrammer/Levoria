package com.sebastian.levoria.item;

import com.google.common.base.Function;
import com.sebastian.levoria.Levoria;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;

public class ModItems {
    //public static final Item EXAMPLE = registerItem("example", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.getId("example"))))); //CLASSIC EXAMPLE
    //public static final Item EXAMPLE = registerEasyItem("example", Item.class, new Item.Settings()); //CUSTOM METHOD EXAMPLE (BOTH DO THE SAME, THIS IS NOT LEGIBLE FOR ITEM WITH CUSTOM ARGUMENTS)

    public static final Item DOWSING_ROD = registerEasyItem("dowsing_rod", DowsingRod.class, new Item.Settings().maxDamage(100).maxCount(1).component(ModDataComponentTypes.LOOKFOR_ORE, 0));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Levoria.getId(name), item);
    }

    public static Item registerEasyItem(String name, Class<? extends Item> item, Item.Settings settings) {
        try {
            return Registry.register(Registries.ITEM, Levoria.getId(name), item.getConstructor(Item.Settings.class).newInstance(settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.getId(name)))));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerModItems() {
        Levoria.LOGGER.info("Registering Mod Items for " + Levoria.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            //entries.add(RAW_PINK_GARNET);
            entries.add(DOWSING_ROD);
        });

    }
}
