package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnButton;
import org.saturnclient.ui.widgets.SaturnImageButton;

import net.minecraft.text.Text;

public class MainMenu extends SaturnUi {
    public MainMenu() {
        super(Text.literal("Saturn Client"));
    }

    @Override
    protected void init() {
        int buttonWidth = 96;
        int buttonHeight = 25;

        draw(
                new SaturnButton("Settings", () -> {
                    client.setScreen(new SettingsMenu());
                }).setX((width - buttonWidth) / 2).setY((height - buttonHeight) / 2).setWidth(buttonWidth)
                        .setHeight(buttonHeight)
                        .setAnimation(SaturnAnimation.FADE_SLIDE.distance(15)));

        draw(
                new SaturnImageButton(Textures.EMOTE, 14, 14,
                        () -> {
                            client.setScreen(new SettingsMenu());
                        })
                        .setX((width - buttonWidth - (buttonHeight * 2) - 6) / 2)
                        .setY((height - buttonHeight) / 2)
                        .setWidth(buttonHeight)
                        .setHeight(buttonHeight)
                        .setAnimation(SaturnAnimation.FADE_SLIDE.distance(15)));

        draw(
                new SaturnImageButton(Textures.COSMETICS, 14, 14,
                        () -> {
                            client.setScreen(new SettingsMenu());
                        })
                        .setX((width / 2) + (buttonWidth / 2) + 3)
                        .setY((height - buttonHeight) / 2)
                        .setWidth(buttonHeight)
                        .setHeight(buttonHeight)
                        .setAnimation(SaturnAnimation.FADE_SLIDE.distance(15)));
    }
}