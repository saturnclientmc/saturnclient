package org.saturnclient.saturnclient.menus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.FadeIn;
import org.saturnclient.ui.animations.Slide;
import org.saturnclient.ui.widgets.SaturnButton;
import org.saturnclient.ui.widgets.SaturnImage;
import org.saturnclient.ui.widgets.SaturnImageButton;

public class MainMenu extends SaturnUi {

    public MainMenu() {
        super(Text.literal("Saturn Client"));
    }

    @Override
    protected void init() {
        SaturnClient.textRenderer = MinecraftClient.getInstance().textRenderer;

        int buttonWidth = 96;
        int buttonHeight = 25;

        draw(
            new SaturnButton("Mods", () -> {
                client.setScreen(new ModMenu());
            })
                .setX((width - buttonWidth) / 2)
                .setY((height - buttonHeight) / 2)
                .setWidth(buttonWidth)
                .setHeight(buttonHeight)
                .setAnimations(new FadeIn(2))
        );

        draw(
            new SaturnImageButton(Textures.HUD_ICON, 14, 14, () -> {
                client.setScreen(new HudEditor());
            })
                .setX((width - buttonWidth - (buttonHeight * 2) - 6) / 2)
                .setY((height - buttonHeight) / 2)
                .setWidth(buttonHeight)
                .setHeight(buttonHeight)
                .setAnimations(new FadeIn(2))
        );

        draw(
            new SaturnImageButton(Textures.COSMETICS, 14, 14, () -> {
                client.setScreen(new CloaksMenu());
            })
                .setX((width / 2) + (buttonWidth / 2) + 3)
                .setY((height - buttonHeight) / 2)
                .setWidth(buttonHeight)
                .setHeight(buttonHeight)
                .setAnimations(new FadeIn(2))
        );

        int logoSize = 49;

        draw(
            new SaturnImage(Textures.LOGO_TEXT)
                .setX((width - logoSize) / 2)
                .setY((height - 8) / 2 - 23)
                .setWidth(logoSize)
                .setHeight(8)
                .setAnimations(new FadeIn(2))
        );

        draw(
            new SaturnImage(Textures.LOGO)
                .setX((width - logoSize) / 2)
                .setY(height / 2 - logoSize - 24)
                .setWidth(logoSize)
                .setHeight(logoSize)
                .setAnimations(new Slide(2, 14))
        );

        super.init();
    }
}
