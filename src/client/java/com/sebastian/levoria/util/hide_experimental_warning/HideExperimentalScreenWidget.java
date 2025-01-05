package com.sebastian.levoria.util.hide_experimental_warning;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.text.Text;

public class HideExperimentalScreenWidget extends ClickableWidget {

    private int progress;
    private final int totalTicks;
    private int ticksPassed;
    private boolean sentSkipMessage;
    private final OnSkipScreen skipScreen;
    private final int screenWidth;

    public HideExperimentalScreenWidget(int x, int y, int screenWidth, int totalTicks, OnSkipScreen skipScreen) {
        super(x, y, 200, 60, Text.literal("Skip Warning"));
        this.totalTicks = totalTicks;
        this.skipScreen = skipScreen;
        this.screenWidth = screenWidth;
    }

    public interface OnSkipScreen {
        void onSkipScreen(HideExperimentalScreenWidget widget);
    }

    public void tickWidget() {
        if (ticksPassed < totalTicks) {
            ticksPassed++;
            progress = (ticksPassed * 100) / totalTicks;
        } else if (!sentSkipMessage) {
            sentSkipMessage = true;
            skipScreen.onSkipScreen(this);
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF0000);

        int barWidth = 200;
        int progressWidth = (int) (barWidth * (progress / 100.0f));


        context.fill(this.getX() + 2, this.getY() + 2, this.getX() + 2 + progressWidth, this.getY() + this.height - 2, 0xFF00FF);

        //context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), this.x + this.width / 2, this.getY() + this.height / 2 - 4, 0xFFFFFF); // Text in der Mitte
        MultilineText.create(MinecraftClient.getInstance().textRenderer, Text.translatable("gui.levoria.skip_warnings"), 200).drawCenterWithShadow(context, screenWidth / 2, getX() + 60);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("narration.levoria.skip_warnings_title"));
        builder.put(NarrationPart.HINT, Text.translatable("narration.levoria.skip_warnings_hint"));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 13) { // 13: Enter
            skipScreen.onSkipScreen(this);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isComplete() {
        return ticksPassed >= totalTicks;
    }
}
