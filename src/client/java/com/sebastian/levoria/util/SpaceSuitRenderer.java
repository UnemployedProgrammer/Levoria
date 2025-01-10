package com.sebastian.levoria.util;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.item.armor.SpaceSuitArmor;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SpaceSuitRenderer extends GeoArmorRenderer<SpaceSuitArmor> {
    public SpaceSuitRenderer() {
        super(new DefaultedItemGeoModel<>(Levoria.id("armor/spacesuit")));
    }

    public static void register() {
        Levoria.LOGGER.info("Registering Space Suit Renderer for #levoria-client");
        ModItems.SPACE_SUIT_HELMET.renderProviderHolder.setValue(new GeoRenderProvider() {
            private SpaceSuitRenderer renderer;

            @Override
            public @Nullable <E extends LivingEntity, S extends BipedEntityRenderState> BipedEntityModel<?> getGeoArmorRenderer(@Nullable E livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, EquipmentModel.LayerType type, BipedEntityModel<S> original) {

                if (this.renderer == null)
                    this.renderer = new SpaceSuitRenderer();

                return this.renderer;
            }
        });
        ModItems.SPACE_SUIT_CHESTPLATE.renderProviderHolder.setValue(new GeoRenderProvider() {
            private SpaceSuitRenderer renderer;

            @Override
            public @Nullable <E extends LivingEntity, S extends BipedEntityRenderState> BipedEntityModel<?> getGeoArmorRenderer(@Nullable E livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, EquipmentModel.LayerType type, BipedEntityModel<S> original) {

                if (this.renderer == null)
                    this.renderer = new SpaceSuitRenderer();

                return this.renderer;
            }
        });
    }
}
