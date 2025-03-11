package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.widgets.SaturnButton;

import net.minecraft.text.Text;

public class MainMenu extends SaturnUi {
    public MainMenu() {
        super(Text.literal("Saturn Client"));
    }

    @Override
    protected void init() {
        draw(
                new SaturnButton("Settings", () -> {
                    client.setScreen(new SettingsMenu());
                }).setX(10).setY(10).setWidth(100).setHeight(20).setAnimation(SaturnAnimation.FADE_SLIDE.distance(15)));
    }
}