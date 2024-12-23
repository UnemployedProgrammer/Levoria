package com.sebastian.levoria.effects;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public class MoonDimensionEffects extends DimensionEffects {
    public MoonDimensionEffects() {
        super(-100, false, SkyType.NORMAL, false, true);
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return new Vec3d(0.0, 0.0, 0.0);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }
}
