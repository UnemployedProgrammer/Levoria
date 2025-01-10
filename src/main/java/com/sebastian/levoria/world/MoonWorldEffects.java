package com.sebastian.levoria.world;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
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

    private static void applyDamage(ServerPlayerEntity player, int ticks) {
        if (ticks >= 16) {
            player.damage(player.getServerWorld(), damage(player.getServerWorld()), 2.0F);
        }
    }


    public static void register() {

        ServerTickEvents.END_WORLD_TICK.register((sW) -> {

            if(sW.isClient)
                return;

            if(sW.getDimension().effects().equals(Levoria.id("moon"))) {
                for (ServerPlayerEntity player : sW.getPlayers()) {
                    boolean shouldTakeDamage = false;

                    ItemStack helmet = player.getInventory().armor.get(3); // Index 3 corresponds to the helmet slot
                    ItemStack chestplate = player.getInventory().armor.get(2); // Index 2 corresponds to the chestplate slot

                    if (!helmet.isOf(ModItems.SPACE_SUIT_HELMET) || helmet.isEmpty()) {
                        shouldTakeDamage = true;
                    }

                    if (chestplate.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                        Integer oxygen = chestplate.get(ModDataComponentTypes.OXYGEN);

                        if (oxygen == null || oxygen <= 0) {
                            shouldTakeDamage = true;
                        } else {
                            chestplate.set(ModDataComponentTypes.OXYGEN, oxygen - 1);
                        }
                    } else {
                        shouldTakeDamage = true;
                    }
                    if(shouldTakeDamage) {
                        applyDamage(player, ticks);
                    }
                }
            }
            ticks++;

            if(ticks == 20) {
                ticks = 0;
            }
        });
    }

    //old code
    /*
    try {
                            if(itemStack.get(DataComponentTypes.EQUIPPABLE).slot().equals(EquipmentSlot.HEAD)) {
                                if(!itemStack.isOf(ModItems.SPACE_SUIT_HELMET)) {
                                    if(ticks < 20 && ticks > 16) {
                                        player.damage(sW, damage(sW), 2f);
                                    }
                                }
                            }

                            if(itemStack.get(DataComponentTypes.EQUIPPABLE).slot().equals(EquipmentSlot.CHEST)) {
                                if(itemStack.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                                    if(itemStack.get(ModDataComponentTypes.OXYGEN) != null) {
                                        int air = itemStack.get(ModDataComponentTypes.OXYGEN);

                                        if(0 >= air) {
                                            System.out.println(ticks);
                                            if(ticks < 20 && ticks > 16) {
                                                player.damage(sW, damage(sW), 2f);
                                            }
                                        } else {
                                            itemStack.set(ModDataComponentTypes.OXYGEN, air - 1);
                                        }
                                    } else {
                                        if(ticks < 20 && ticks > 16) {
                                            player.damage(sW, damage(sW), 2f);
                                        }
                                    }
                                } else {
                                    if(ticks < 20 && ticks > 16) {
                                        player.damage(sW, damage(sW), 2f);
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
     */

    //old code #2
    /*
    for (ItemStack itemStack : player.getArmorItems()) {
                        EquipmentSlot slot = EquipmentSlot.MAINHAND;
                        try {
                            slot = itemStack.get(DataComponentTypes.EQUIPPABLE).slot();
                        } catch (Exception e) {
                            //Item doesn't have Equippable Property
                            //Levoria.LOGGER.info(e.toString());
                        }

                        if (slot == EquipmentSlot.HEAD) {
                            if (!itemStack.isOf(ModItems.SPACE_SUIT_HELMET) || itemStack.isEmpty()) {
                                shouldTakeDamage = true;
                            }
                        }

                        if (slot == EquipmentSlot.CHEST) {
                            if (itemStack.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                                Integer oxygen = itemStack.get(ModDataComponentTypes.OXYGEN);

                                if (oxygen == null || oxygen <= 0) {
                                    shouldTakeDamage = true;
                                    System.out.println("no air");
                                } else {
                                    itemStack.set(ModDataComponentTypes.OXYGEN, oxygen - 1);
                                    System.out.println("air");
                                }
                            } else {
                                System.out.println("Not Spacesuit!");
                                shouldTakeDamage = true;
                            }
                        }
                    }
     */
}
