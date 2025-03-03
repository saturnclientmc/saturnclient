package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.widgets.AuraButton;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MainMenu extends Screen {
    public MainMenu() {
        super(Text.literal("Aura Client"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 50;
        int buttonHeight = 30;

        addDrawableChild(
                new AuraButton(Text.literal("Cloaks"), (_button) -> {
                    if (client.player != null) {
                        client.setScreen(new CloakSelector());
                    }
                }, (this.width - buttonWidth) / 2, (this.height - buttonHeight) / 2, buttonWidth, buttonHeight));
    }
}