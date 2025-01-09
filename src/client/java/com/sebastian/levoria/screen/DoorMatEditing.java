package com.sebastian.levoria.screen;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.network.specific.ApplyEditedDoorMatC2S;
import com.sebastian.levoria.screen.element.CenteredTextTextbox;
import com.sebastian.levoria.util.DropDownManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;

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
    private DropDownManager translatableSelections;

    public DoorMatEditing(BlockPos pos, String beCurrentText) {
        super(Text.translatable("gui.levoria.doormat.title"));
        this.pos = pos;
        this.beCurrentText = beCurrentText;
        this.translated = beCurrentText.contains("translate->");
        this.currentText = beCurrentText;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderLayer::getGuiTextured, Levoria.id("textures/gui/doormat_x140_100.png"), width / 2 - 70, height / 2 - 50, 0, 0, 140, 100, 140, 100);
        super.render(context, mouseX, mouseY, delta);
        if(translated) {
            context.drawCenteredTextWithShadow(textRenderer, Text.translatable(currentText.replace("translate->", "")), width / 2, height / 2, 0xFFFFFF);
        }
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("gui.levoria.doormat.title"), width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void init() {
        mode_translated = addDrawableChild(ButtonWidget.builder(translated ? Text.literal("> ").append(Text.translatable("gui.levoria.doormat.translated")) : Text.translatable("gui.levoria.doormat.translated"), (btn) -> {
            mode_literal.setMessage(Text.translatable("gui.levoria.doormat.literal"));
            btn.setMessage(Text.literal("> ").append(Text.translatable("gui.levoria.doormat.translated")));
            translated = true;
            currentText = "translate->doormat.levoria.welcome";
            textField.active = false;
            textField.visible = false;
            textField.setFocused(false);
            translatableSelections.setVisible(true);
        }).dimensions(20, height / 2 - 50, 100, 20).build());

        mode_literal = addDrawableChild(ButtonWidget.builder(!translated ? Text.literal("> ").append(Text.translatable("gui.levoria.doormat.literal")) : Text.translatable("gui.levoria.doormat.literal"), (btn) -> {
            btn.setMessage(Text.literal("> ").append(Text.translatable("gui.levoria.doormat.literal")));
            mode_translated.setMessage(Text.translatable("gui.levoria.doormat.translated"));
            translated = false;
            currentText = Text.translatable(currentText.replace("translate->", "")).getString();
            textField.setText(currentText);
            textField.active = true;
            textField.visible = true;
            textField.setFocused(true);
            translatableSelections.setVisible(false);
        }).dimensions(20, height / 2 - 20, 100, 20).build());

        textField = addDrawableChild(new CenteredTextTextbox(textRenderer, width / 2 - textFieldOffset, height / 2 - 10, 120, 20, Text.literal("Ex. Welcome Home..."), Text.literal(currentText), new CenteredTextTextbox.OnChangeListener() {
            @Override
            public void onChange(String str) {
                ClientPlayNetworking.send(new ApplyEditedDoorMatC2S(str, pos));
            }
        }));

        translatableSelections = new DropDownManager(width - 120, height / 2 - 50, 100, Text.translatable("gui.levoria.doormat.text_collection"), List.of(
                new Pair<>("doormat.levoria.welcome", Text.translatable("doormat.levoria.welcome")),
                new Pair<>("doormat.levoria.shoes_off", Text.translatable("doormat.levoria.shoes_off")),
                new Pair<>("doormat.levoria.home_sweet", Text.translatable("doormat.levoria.home_sweet"))
        ));

        for (ButtonWidget buttonWidget : translatableSelections.getButtonsToAdd()) {
            addDrawableChild(buttonWidget);
        }

        translatableSelections.setChangeListener((str) -> {
            currentText = "translate->" + str;
            ClientPlayNetworking.send(new ApplyEditedDoorMatC2S(currentText, pos));
        });

        if(translated) {
            textField.active = false;
            textField.visible = false;
            textField.setFocused(false);
            translatableSelections.setVisible(true);
        } else {
            currentText = Text.translatable(currentText.replace("translate->", "")).getString();
            textField.setText(currentText);
            textField.active = true;
            textField.visible = true;
            textField.setFocused(true);
            translatableSelections.setVisible(false);
        }

        textField.setCharLimit(20);

        super.init();
    }

    @Override
    public void tick() {
        textField.tick();
        super.tick();
    }

    @Override
    public boolean shouldPause() {
        return false;
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