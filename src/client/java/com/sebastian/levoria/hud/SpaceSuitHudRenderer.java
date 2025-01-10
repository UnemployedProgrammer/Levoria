package com.sebastian.levoria.hud;

import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.util.TickConverter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ColorHelper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpaceSuitHudRenderer {

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if(!MinecraftClient.getInstance().options.hudHidden && wearsSpaceSuit()) {
                int air = getAirLeft();
                Pair<String, Integer> p = getStringAndColor(air);
                TooltipBackgroundRenderer.render(context, 5, 5, 150, 25, 0, null);
                float percentage = percentageOf(air, 72000);
                int width = calculatePercentage(percentage, 144);
                //System.out.println(percentage + "% | " + width + " of 152");
                context.fill(8, 20, 8 + width, 30, 0, ColorHelper.fullAlpha(p.getRight()));
                context.drawText(MinecraftClient.getInstance().textRenderer, Text.translatable("gui.levoria.oxygen_left").getString().replace("{}", p.getLeft()), 8, 8, 0xFFFFFF, false);
            }
        });
    }

    public static Pair<String, Integer> getStringAndColor(int air) {
        int red = 0xD63A3A;
        int yellow = 0xFCE94F;
        int green = 0x73D216;
        int color = green;

        if(air <= 24000) {
            color = yellow;
        }
        if(air <= 6000) {
            color = red;
        }

        return new Pair<>(TickConverter.convertTicksToTime(air), color);
    }

    public static int getAirLeft() {
       AtomicInteger r = new AtomicInteger();
       if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getArmorItems() != null) {
           MinecraftClient.getInstance().player.getArmorItems().forEach((stack) -> {
               if(stack.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                   if(stack.get(ModDataComponentTypes.OXYGEN) != null) {
                       r.set(stack.get(ModDataComponentTypes.OXYGEN));
                   }
               }
           });
       }
       return r.get();
    }

    public static Boolean wearsSpaceSuit() {
        AtomicBoolean r = new AtomicBoolean(false);
        if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getArmorItems() != null) {
            MinecraftClient.getInstance().player.getArmorItems().forEach((stack) -> {
                if(stack.isOf(ModItems.SPACE_SUIT_CHESTPLATE)) {
                    r.set(true);
                }
            });
        }
        return r.get();
    }

    // Berechnet, wie viel Prozent a von b ist (als float-Wert)
    public static float percentageOf(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Denominator (b) cannot be zero.");
        }
        return (a * 100.0f) / b;
    }

    // Berechnet den Wert, der einem gegebenen Prozentwert (percent) von total entspricht
    public static int calculatePercentage(float percent, int total) {
        return Math.round((percent * total) / 100.0f);
    }

}
