package com.sebastian.levoria.world.tree;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.world.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator SHADOWWOOD = new SaplingGenerator(Levoria.MOD_ID + ":shadowwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.SHADOW_WOOD_KEY), Optional.empty());
}
