package org.saturnclient.saturnclient.menus;

import org.saturnclient.saturnclient.widgets.SaturnButton;
import org.saturnclient.saturnclient.widgets.SaturnClientTexture;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MainMenu extends Screen {
    public MainMenu() {
        super(Text.literal("Saturn Client"));
    }

    @Override
    protected void init() {
        super.init();
        int saturnClientSize = 40;
        int saturnClientX = (this.width - saturnClientSize) / 2;
        int saturnClientY = ((this.height - 110) - saturnClientSize) / 2;

        TextWidget saturnClientText = new TextWidget(Text.literal("Saturn Client"), this.textRenderer);

        saturnClientText.setX((this.width - saturnClientText.getWidth()) / 2);
        saturnClientText.setY(saturnClientY + saturnClientSize + 3);

        addDrawable(saturnClientText);

        addDrawable(SaturnClientTexture.builder(Identifier.of("saturnclient", "icon.png"))
                .dimensions(saturnClientX, saturnClientY, saturnClientSize, saturnClientSize));

        int buttonWidth = 50;
        int buttonHeight = 30;

        addDrawableChild(
                new SaturnButton(Text.literal("Cloaks"), (_button) -> {
                    if (client.player != null) {
                        client.setScreen(new CloakSelector());
                    }
                }, (this.width - buttonWidth) / 2, (this.height - buttonHeight) / 2, buttonWidth, buttonHeight));
    }
}