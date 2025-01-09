package com.sebastian.levoria.screen.element;

import com.sebastian.levoria.util.DropDownManager;
import com.sebastian.levoria.util.ShakeAnimation;
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
import net.minecraft.client.input.KeyCodes;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class CenteredTextTextbox extends ClickableWidget {
    private final int xCenter;
    private final int yCenter;
    private final EditBox box;
    private int blink;
    private OnChangeListener listener;
    private ShakeAnimation anim;

    public static interface OnChangeListener {
        public void onChange(String str);
    }

    private static class EmptyChangeListener implements OnChangeListener {
        @Override
        public void onChange(String str) {

        }
    }

    public static EmptyChangeListener EMPTY_LISTENER = new EmptyChangeListener();

    public CenteredTextTextbox(TextRenderer textRenderer, int xCenter, int yCenter, int width, int height, Text placeholder, Text message, OnChangeListener l) {
        super(xCenter - width / 2, yCenter - height / 2, width, height, message);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        box = new EditBox(textRenderer, width);
        blink = 20;
        listener = l;
        box.setChangeListener((s -> listener.onChange(s)));
        anim = new ShakeAnimation();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        anim.translateMatrix(context.getMatrices());

        context.enableScissor(getX(), getY(), getX() + width, getY() + height);
        String blinkText = (blink > 20) && (blink <= 40) && isSelected() ? "" : ""; //"|", " "
        int cursorOffset = getCursorOffset(box.getText());
        int xTextStart = xCenter - MinecraftClient.getInstance().textRenderer.getWidth(box.getText()) / 2;
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, box.getText() + blinkText, xCenter, yCenter, anim.isAnimating() ? 0xEC5858 : 0xF2F2F2);
        context.disableScissor();
        if((blink > 15) && (blink <= 30)) {
            context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("|"), xTextStart + cursorOffset, yCenter + 1, anim.isAnimating() ? 0xEC5858 : 0xF2F2F2, false); // xTextStart + cursorOffset + 10, getY() + 2
        }
        context.getMatrices().pop();
        anim.updateAnimation();
    }

    private int getCursorOffset(String text) {
        int r = 0;
        int ind = 0;
        for (char c : text.toCharArray()) {
            if(ind != box.getCursor()) {
                r += MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(c));
                ind++;
            }
        }
        return r;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, "Textbox");
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.visible && this.isFocused() && StringHelper.isValidChar(chr)) {
            this.box.replaceSelection(Character.toString(chr));
            if(this.box.getMaxLength() == this.box.getText().length()) {
                anim.startAnimation();
            }
            return true;
        } else {
            return false;
        }
    }

    public void tick() {
        blink++;
        if (blink == 31) {
            blink = 1;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ENTER) {
            return false;
        }
        return this.box.handleSpecialKey(keyCode);
    }

    public void setCharLimit(int charLimit) {
        box.setMaxLength(charLimit);
    }

    public void setText(String msg) {
        box.setText(msg);
    }
}
