package org.saturnclient.saturnclient.menus;

import org.saturnclient.saturnclient.widgets.SaturnButton;
import org.saturnclient.saturnclient.widgets.SaturnText;
import org.saturnclient.saturnclient.widgets.SaturnTexture;

import net.minecraft.client.gui.screen.Screen;
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
        int saturnClientY = ((this.height - 105) - saturnClientSize) / 2;

        addDrawable(new SaturnText("Saturn Client")
                .position((this.width - this.textRenderer.getWidth("Saturn Client")) / 2,
                        saturnClientY + saturnClientSize + 1)
                .setAlpha(0)
                .onRender(t -> {
                    if (t.alpha < 1.0f) {
                        t.alpha += 0.1f;
                    }
                }));

        addDrawable(SaturnTexture.builder(Identifier.of("saturnclient", "icon.png"))
                .dimensions(saturnClientX, saturnClientY + 20, saturnClientSize, saturnClientSize).onRender(t -> {
                    if (t.y > saturnClientY) {
                        t.y -= 1;
                    }
                }));

        int buttonWidth = 50;
        int buttonHeight = 30;

        SaturnButton cloakButton = SaturnButton.builder(Text.literal("Cloaks"), (_button) -> {
            if (client.player != null) {
                client.setScreen(new CloakSelector());
            }
        }, (this.width - buttonWidth) / 2, (this.height - buttonHeight) / 2, buttonWidth, buttonHeight);

        addDrawableChild(cloakButton);
    }
}