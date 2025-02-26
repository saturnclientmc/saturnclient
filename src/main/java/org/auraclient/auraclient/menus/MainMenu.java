package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MainMenu extends Screen {
    public MainMenu() {
        super(Text.literal("Custom Screen"));
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.literal("Cloaks"), (_button) -> {
            client.setScreen(new CloakSelector());
        }).dimensions((this.width - 50) / 2, (this.height - 30) / 2, 50, 30).build());
    }
}