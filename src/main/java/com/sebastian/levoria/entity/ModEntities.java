package com.sebastian.levoria.entity;

import com.sebastian.levoria.Levoria;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<RocketEntity> ROCKET = Registry.register(Registries.ENTITY_TYPE,
            Levoria.id("rocket"),
            EntityType.Builder.create(RocketEntity::new, SpawnGroup.MISC)
                    .dimensions(1.3f, 4.5f).eyeHeight(0.5625F).dropsNothing().maxTrackingRange(10).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Levoria.id("rocket"))));



    public static void registerModEntities() {
        Levoria.LOGGER.info("Registering Mod Entities for " + Levoria.MOD_ID);
    }
}