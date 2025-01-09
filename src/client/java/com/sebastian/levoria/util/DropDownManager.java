package com.sebastian.levoria.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DropDownManager {
    private ButtonWidget mainWidget;
    private List<ButtonWidget> optionsBtns = new ArrayList<>();
    private final int x, y, buttonWidth;
    private String value = "";
    private ChangeListener listener = null;


    public DropDownManager(int x, int y, int buttonWidth, Text title, List<Pair<String, Text>> options) {
        this.x = x;
        this.y = y;
        this.buttonWidth = buttonWidth;
        mainWidget = ButtonWidget.builder(title, (btn) -> {
            if(btn.active) {
                btn.active = false;
                expand();
            }
        }).dimensions(x, y, buttonWidth, 20).build();
        AtomicInteger basicHeight = new AtomicInteger(y + 25);
        for (Pair<String, Text> option : options) {
            optionsBtns.add(ButtonWidget.builder(option.getRight(), (btn) -> {
                mainWidget.active = true;
                shrink();
                value = option.getLeft();
                if(listener != null) {
                    listener.change(option.getLeft());
                }
            }).dimensions(x, basicHeight.get(), buttonWidth, 20).build());
            basicHeight.addAndGet(20);
        }
        shrink();
    }

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void expand() {
        for (ButtonWidget optionsBtn : optionsBtns) {
            optionsBtn.visible = true;
            optionsBtn.active = true;
        }
    }

    public void setVisible(boolean visible) {
        for (ButtonWidget optionsBtn : optionsBtns) {
            optionsBtn.visible = false;
            optionsBtn.active = false;
        }
        mainWidget.visible = visible;
        mainWidget.active = visible;
    }

    public void shrink() {
        for (ButtonWidget optionsBtn : optionsBtns) {
            optionsBtn.visible = false;
            optionsBtn.active = false;
        }
    }

    public List<ButtonWidget> getButtonsToAdd() {
        List<ButtonWidget> r = new ArrayList<>(List.copyOf(optionsBtns));
        r.add(mainWidget);
        return r;
    }

    @Environment(EnvType.CLIENT)
    public interface ChangeListener {
        void change(String value);
    }
}
