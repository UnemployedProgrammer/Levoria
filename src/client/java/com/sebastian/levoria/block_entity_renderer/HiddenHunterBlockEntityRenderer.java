package com.sebastian.levoria.block_entity_renderer;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.entity.HiddenHunterBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HiddenHunterBlockEntityRenderer extends GeoBlockRenderer<HiddenHunterBlockEntity> {
    public HiddenHunterBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new DefaultedBlockGeoModel<>(Levoria.getId("hidden_hunter")));
    }


}
