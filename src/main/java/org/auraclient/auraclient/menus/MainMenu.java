package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.widgets.AuraButton;
import org.auraclient.auraclient.widgets.AuraClientTexture;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MainMenu extends Screen {
    public MainMenu() {
        super(Text.literal("Aura Client"));
    }

    @Override
    protected void init() {
        super.init();
        int auraClientSize = 40;
        int auraClientX = (this.width - auraClientSize) / 2;
        int auraClientY = ((this.height - 110) - auraClientSize) / 2;

        TextWidget auraClientText = new TextWidget(Text.literal("Saturn Client"), this.textRenderer);

        auraClientText.setX((this.width - auraClientText.getWidth()) / 2);
        auraClientText.setY(auraClientY + auraClientSize + 3);

        addDrawable(auraClientText);

        addDrawable(AuraClientTexture.builder(Identifier.of("auraclient", "icon.png"))
                .dimensions(auraClientX, auraClientY, auraClientSize, auraClientSize));

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