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

        int tabsX = (width - 43) / 2;
        int tabsY = modsY - 17;

        draw(new SaturnTexture(Textures.TABS)
                .setX(tabsX)
                .setY(tabsY)
                .setWidth(43)
                .setHeight(15)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        int tabSize = 9;
        tabsX = (width - tabSize) / 2;
        draw(new SaturnClickableImage(Textures.SETTINGS_TAB, () -> {
            System.out.println("Pressed");
        })
                .setX(tabsX)
                .setY(tabsY + 3)
                .setWidth(tabSize)
                .setHeight(tabSize)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        draw(new SaturnClickableImage(Textures.MODS_TAB, () -> {
            System.out.println("Pressed");
        })
                .setX(tabsX - 12)
                .setY(tabsY + 3)
                .setWidth(tabSize)
                .setHeight(tabSize)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        draw(new SaturnClickableImage(Textures.SEARCH, () -> {
            System.out.println("Pressed");
        })
                .setX(tabsX + 12)
                .setY(tabsY + 3)
                .setWidth(tabSize)
                .setHeight(tabSize)
                .setAnimation(SaturnAnimation.FADE_SLIDE));

        int modX = modsX + 15;
        int modY = modsY + 10;
        int col = 0;

        for (int i = 0; i < 10; i++) {
            draw(new SaturnClickableImage(Textures.MOD, (m) -> {
            })
                    .setX(modX)
                    .setY(modY)
                    .setWidth(86)
                    .setHeight(20));

            col++;

            if (col < 3) {
                modX += 86 + 15;
            } else {
                modX = modsX + 15;
                modY += 20 + 20;
                col = 0;
            }
        }
    }
}