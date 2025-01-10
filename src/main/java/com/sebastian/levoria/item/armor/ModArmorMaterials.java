package com.sebastian.levoria.item.armor;

import com.sebastian.levoria.Levoria;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentModels;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

import static com.ibm.icu.impl.SortedSetRelation.A;

public class ModArmorMaterials  {
    public static ArmorMaterial SPACESUIT = new ArmorMaterial(15, Util.make(new EnumMap(EquipmentType.class), map -> {
        map.put(EquipmentType.BOOTS, 2);
        map.put(EquipmentType.LEGGINGS, 5);
        map.put(EquipmentType.CHESTPLATE, 6);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 5);
    }), 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.1F, 0.5F, ItemTags.REPAIRS_IRON_ARMOR, Levoria.id("space_suit"));
}