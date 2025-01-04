package com.sebastian.levoria.enchantment;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.enchantment.custom.DowsingRodReachEnchantment;
import com.sebastian.levoria.tags.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> REACH =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Levoria.id("reach"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, REACH, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.REACH_ENCHANTABLE),
                        items.getOrThrow(ModTags.Items.REACH_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.leveledCost(2, 3),
                        Enchantment.leveledCost(10, 4),
                        1,
                        AttributeModifierSlot.MAINHAND))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new DowsingRodReachEnchantment()));
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
