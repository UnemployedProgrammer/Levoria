package com.sebastian.levoria.screen;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.screen.element.CenteredTextTextbox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DoorMatEditing extends Screen {

    private final BlockPos pos;
    private final String beCurrentText;

    private String currentText;

    private boolean translated;
    private boolean canEdit;

    private ButtonWidget mode_translated;
    private ButtonWidget mode_literal;
    private CenteredTextTextbox textField;
    private int textFieldOffset = 0;

    public interface OnChangeListener {
        public void onChange(String str);
    }

    public DoorMatEditing(BlockPos pos, String beCurrentText) {
        super(Text.literal(""));
        this.pos = pos;
        this.beCurrentText = beCurrentText;
        this.translated = beCurrentText.contains("translate->");
        this.currentText = beCurrentText;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderLayer::getGuiTextured, Levoria.id("textures/gui/doormat_x140_100.png"), width / 2 - 70, height / 2 - 50, 0, 0, 140, 100, 140, 100);
        if(translated) {
            context.drawCenteredTextWithShadow(textRenderer, Text.translatable(currentText.replace("translate->", "")), width / 2, height / 2, 0xFFFFFF);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void init() {
        mode_translated = addDrawableChild(ButtonWidget.builder(translated ? Text.literal("> Translated Text") : Text.literal("Translated Text"), (btn) -> {
            mode_literal.setMessage(Text.literal("Literal Text"));
            btn.setMessage(Text.literal("> Translated Text"));
            translated = true;
            currentText = "translate->doormat.levoria.welcome";
            textField.active = false;
            textField.visible = false;
            textField.setFocused(false);
        }).dimensions(20, 20, 100, 20).build());

        mode_literal = addDrawableChild(ButtonWidget.builder(!translated ? Text.literal("> Literal Text") : Text.literal("Literal Text"), (btn) -> {
            btn.setMessage(Text.literal("> Literal Text"));
            mode_translated.setMessage(Text.literal("Translated Text"));
            translated = false;
            currentText = Text.translatable(currentText.replace("translate->", "")).getString();
            textField.setText(currentText);
            textField.active = true;
            textField.visible = true;
            textField.setFocused(true);
        }).dimensions(20, 50, 100, 20).build());

        textField = addDrawableChild(new CenteredTextTextbox(textRenderer, width / 2 - textFieldOffset, height / 2 - 10, 120, 20, Text.literal("Ex. Welcome Home..."), Text.literal(currentText)));

        if(translated) {
            textField.visible = false;
        }

        textField.setCharLimit(20);

        super.init();
    }

    @Override
    public void tick() {
        textField.tick();
        super.tick();
    }

    //@Override
    //    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    //        textField.keyPressed(keyCode, scanCode, modifiers);
    //        return super.keyPressed(keyCode, scanCode, modifiers);
    //    }
    //
    //    @Override
    //    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    //        textField.keyReleased(keyCode, scanCode, modifiers);
    //        return super.keyReleased(keyCode, scanCode, modifiers);
    //    }
    //
    //    @Override
    //    public boolean mouseClicked(double mouseX, double mouseY, int button) {
    //        textField.mouseClicked(mouseX, mouseY, button);
    //        return super.mouseClicked(mouseX, mouseY, button);
    //    }
    //
    //    @Override
    //    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    //        textField.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    //        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    //    }
}