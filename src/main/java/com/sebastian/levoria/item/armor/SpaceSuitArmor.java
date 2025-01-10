package com.sebastian.levoria.item.armor;

import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.util.TickConverter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.event.listener.GameEventListener;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

public final class SpaceSuitArmor extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final MutableObject<GeoRenderProvider> renderProviderHolder = new MutableObject<>();

    public SpaceSuitArmor(ArmorMaterial material, EquipmentType type, Settings settings) {
        super(material, type, settings);
    }


    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        // Return the cached RenderProvider
        consumer.accept(this.renderProviderHolder.getValue());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 20, state -> {
            // Apply our generic idle animation.
            // Whether it plays or not is decided down below.
            state.setAnimation(DefaultAnimations.IDLE);

            // Let's gather some data from the state to use below
            // This is the entity that is currently wearing/holding the item
            Entity entity = state.getData(DataTickets.ENTITY);

            // We'll just have ArmorStands always animate, so we can return here
            if (entity instanceof ArmorStandEntity || !(entity instanceof LivingEntity owner))
                return PlayState.CONTINUE;

            // For this example, we only want the animation to play if the entity is wearing all pieces of the armor
            // Let's collect the armor pieces the entity is currently wearing
            Set<Item> wornArmor = new ObjectOpenHashSet<>();

            for (ItemStack stack : owner.getArmorItems()) {
                // We can stop immediately if any of the slots are empty
                if (stack.isEmpty())
                    return PlayState.STOP;

                wornArmor.add(stack.getItem());
            }

            // Check each of the pieces match our set
            boolean isFullSet = wornArmor.containsAll(ObjectArrayList.of(
                    ModItems.SPACE_SUIT_HELMET,
                    ModItems.SPACE_SUIT_CHESTPLATE));

            // Play the animation if the full set is being worn, otherwise stop
            return isFullSet ? PlayState.CONTINUE : PlayState.STOP;
        }));
    }

    @Override
    public Object getRenderProvider() {
        return GeoItem.super.getRenderProvider();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        try {
            if(stack.get(DataComponentTypes.EQUIPPABLE).slot().equals(EquipmentSlot.CHEST)) {

                int val = 0;
                int maxVal = 72000;

                if(stack.get(ModDataComponentTypes.OXYGEN) != null) {
                    val = stack.get(ModDataComponentTypes.OXYGEN).intValue();
                }

                int filledBars = MathHelper.floor(val / (float) maxVal * 10); // 10 Balken insgesamt
                StringBuilder progressBar = new StringBuilder("[");

                // Generiere die Balken (gefüllt oder leer)
                for (int i = 0; i < 10; i++) {
                    if (i < filledBars) {
                        progressBar.append("█"); // Gefüllter Balken
                    } else {
                        progressBar.append("░"); // Leerer Balken
                    }
                }
                progressBar.append("]");

                tooltip.add(Text.literal(progressBar.toString() + " " + (int)((float)val / maxVal * 100) + "%").formatted(getColor(val)));
            }
        } catch (Exception ignored) {
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public static Formatting getColor(int air) {
        Formatting red = Formatting.RED;
        Formatting yellow = Formatting.YELLOW;
        Formatting color = Formatting.GREEN;

        if(air <= 24000) {
            color = yellow;
        }
        if(air <= 6000) {
            color = red;
        }

        return color;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }



}
