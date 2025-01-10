package com.sebastian.levoria.item;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.item.armor.ModArmorMaterials;
import com.sebastian.levoria.item.armor.SpaceSuitArmor;
import com.sebastian.levoria.util.AdvancedRegisterItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.lang.reflect.InvocationTargetException;

public class ModItems {
    //public static final Item EXAMPLE = registerItem("example", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.getId("example"))))); //CLASSIC EXAMPLE
    //public static final Item EXAMPLE = registerEasyItem("example", Item.class, new Item.Settings()); //CUSTOM METHOD EXAMPLE (BOTH DO THE SAME, THIS IS NOT LEGIBLE FOR ITEM WITH CUSTOM ARGUMENTS)

    public static final Item DOWSING_ROD = registerEasyItem("dowsing_rod", DowsingRod.class, new Item.Settings().maxDamage(100).maxCount(1).component(ModDataComponentTypes.LOOKFOR_ORE, 0));
    public static final Item MOON_BERRIES = registerItem("moon_berries", new BlockItem(ModBlocks.MOON_BERRY_BUSH, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id("moon_berries"))).useItemPrefixedTranslationKey().food(new FoodComponent.Builder().nutrition(6).saturationModifier(4f).alwaysEdible().build())));

    public static final SpaceSuitArmor SPACE_SUIT_HELMET = AdvancedRegisterItem.register("space_suit_helmet", new SpaceSuitArmor(ModArmorMaterials.SPACESUIT, EquipmentType.HELMET, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id("space_suit_helmet")))));
    public static final SpaceSuitArmor SPACE_SUIT_CHESTPLATE = AdvancedRegisterItem.register("space_suit_chestplate", new SpaceSuitArmor(ModArmorMaterials.SPACESUIT, EquipmentType.CHESTPLATE, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id("space_suit_chestplate")))));

    public static Item registerItem(String name, Item item) {
        return ModItemGroups.add(Registry.register(Registries.ITEM, Levoria.id(name), item));
    }

    public static Item registerEasyItem(String name, Class<? extends Item> item, Item.Settings settings) {
        try {
            return ModItemGroups.add( Registry.register(Registries.ITEM, Levoria.id(name), item.getConstructor(Item.Settings.class).newInstance(settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Levoria.id(name))))) );
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerModItems() {
        Levoria.LOGGER.info("Registering Mod Items for " + Levoria.MOD_ID);

        /*
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            //entries.add(RAW_PINK_GARNET);
            entries.add(DOWSING_ROD);
        });
         */

    }
}
