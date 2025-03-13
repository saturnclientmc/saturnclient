package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnTexture;
import org.saturnclient.ui.SaturnAnimation;

import net.minecraft.text.Text;

public class SettingsMenu extends SaturnUi {
    public SettingsMenu() {
        super(Text.literal("Settings"));
    }

    @Override
    protected void init() {
        int rectWidth = 330;
        int rectHeight = 228;
        int modsX = (width - rectWidth) / 2;
        int modsY = (height - rectHeight) / 2;

        draw(new SaturnTexture(Textures.SETTINGS_BG)
                .setX(modsX)
                .setY(modsY)
                .setWidth(rectWidth)
                .setHeight(rectHeight)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        draw(new SaturnTexture(Textures.TABS)
                .setX((width - 43) / 2)
                .setY(modsY - 16)
                .setWidth(43)
                .setHeight(15)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        int modX = modsX + 15;

        for (int i = 0; i < 3; i++) {
            draw(new SaturnClickableImage(Textures.MOD, () -> {
                System.out.println("Pressed");
            })
                    .setBackground(false)
                    .setX(modX)
                    .setY(modsY + 10)
                    .setWidth(86)
                    .setHeight(20)
                    .setAnimation(SaturnAnimation.FADE_SLIDE.offset(40)));

            modX += 86 + 15;
        }
    }
}