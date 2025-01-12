package com.sebastian.levoria.entity_renderers;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.entity.RocketEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RocketEntityRenderer extends GeoEntityRenderer<RocketEntity> {
    public RocketEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(Levoria.id("rocket"), false));
    }
}
