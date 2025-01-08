package com.sebastian.levoria.screen.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.EditBox;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;

public class CenteredTextTextbox extends ClickableWidget {
    private final int xCenter;
    private final int yCenter;
    private final EditBox box;
    private int blink;

    public CenteredTextTextbox(TextRenderer textRenderer, int xCenter, int yCenter, int width, int height, Text placeholder, Text message) {
        super(xCenter - width / 2, yCenter - height / 2, width, height, message);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        box = new EditBox(textRenderer, width);
        blink = 20;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.enableScissor(getX(), getY(), getX() + width, getY() + height);
        String blinkText = (blink > 20) && (blink <= 40) && isSelected() ? "" : ""; //"|", " "
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, box.getText() + blinkText, xCenter, yCenter, 0xF2F2F2);
        context.disableScissor();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, "Textbox");
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.visible && this.isFocused() && StringHelper.isValidChar(chr)) {
            this.box.replaceSelection(Character.toString(chr));
            return true;
        } else {
            return false;
        }
    }

    public void tick() {
        blink++;
        if(blink == 41) {
            blink = 1;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.box.handleSpecialKey(keyCode);
    }

    public void setCharLimit(int charLimit) {
        box.setMaxLength(charLimit);
    }

    public void setText(String msg) {
        box.setText(msg);
    }
}
