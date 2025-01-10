package com.sebastian.levoria.world;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class MoonWorldEffects {

    public static int ticks = 0;

    public static DamageSource damage(ServerWorld w) {
        Registry<DamageType> registry = w.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE);
        RegistryEntry<DamageType> val = registry.getOrThrow(Levoria.NO_OXYGEN_DAMAGE);

        return new DamageSource(val);
    }

    public static void register() {

        ServerTickEvents.END_WORLD_TICK.register((sW) -> {

            if(sW.isClient)
                return;

            if(sW.getDimension().effects().equals(Levoria.id("moon"))) {
                for (ServerPlayerEntity player : sW.getPlayers()) {
                    player.getArmorItems().forEach((itemStack -> {
                        try {
                            if(itemStack.get(DataComponentTypes.EQUIPPABLE).slot().equals(EquipmentSlot.CHEST)) {
                                if(itemStack.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                                    if(itemStack.get(ModDataComponentTypes.OXYGEN) != null) {
                                        int air = itemStack.get(ModDataComponentTypes.OXYGEN);

                                        if(air <= 0) {
                                            System.out.println(ticks);
                                            if(ticks < 20 && ticks > 16) {
                                                player.damage(sW, damage(sW), 2f);
                                            }
                                        } else {
                                            itemStack.set(ModDataComponentTypes.OXYGEN, air - 1);
                                        }
                                    }
                                } else {
                                    if(ticks < 20 && ticks > 16) {
                                        player.damage(sW, damage(sW), 2f);
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
                    }));
                }
                ticks++;

                if(ticks == 20) {
                    ticks = 0;
                }
            }

        });
    }
}
