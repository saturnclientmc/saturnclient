package org.saturnclient.saturnclient.menus;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnSprite;

public class SaturnConfigEditor extends ConfigEditor {

    public SaturnConfigEditor() {
        super(SaturnClient.config);
    }

    @Override
    protected void init() {
        int rectHeight = 228;
        int rectY = (height - rectHeight + 15) / 2;

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(
                new SaturnSprite(Textures.RECT)
                        .setX(tabsX)
                        .setY(tabsY)
                        .setWidth(43)
                        .setHeight(15)
                        .setAnimations(SaturnClient.getAnimations()));

        int tabSize = 9;
        tabsX = (width - tabSize) / 2;
        draw(
                new SaturnClickableImage(Textures.SETTINGS, () -> {
                })
                        .setSelected(true)
                        .setX(tabsX)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.MODS_TAB, () -> {
                    client.setScreen(new ModMenu());
                })
                        .setX(tabsX - 12)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.SEARCH, () -> {
                    System.out.println("Pressed");
                })
                        .setX(tabsX + 12)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClient.getAnimations()));

        super.init();
    }
}
