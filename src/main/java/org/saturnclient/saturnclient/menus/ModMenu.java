package org.saturnclient.saturnclient.menus;

import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnmods.ModManager;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.components.SaturnModComp;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

public class ModMenu extends SaturnUi {

    public ModMenu() {
        super(Text.literal("Mods"));
    }

    @Override
    protected void init() {
        int rectWidth = 330;
        int rectHeight = 228;
        int modsX = (width - rectWidth) / 2;
        int modsY = (height - rectHeight + 15) / 2;

        draw(
                new SaturnSprite(Textures.SETTINGS_BG)
                        .setX(modsX)
                        .setY(modsY)
                        .setWidth(rectWidth)
                        .setHeight(rectHeight)
                        .setAnimations(SaturnClient.getAnimations()));

        int tabsX = (width - 43) / 2;
        int tabsY = modsY - 17;

        draw(
                new SaturnSprite(Textures.TABS)
                        .setX(tabsX)
                        .setY(tabsY)
                        .setWidth(43)
                        .setHeight(15)
                        .setAnimations(SaturnClient.getAnimations()));

        int tabSize = 9;
        tabsX = (width - tabSize) / 2;
        draw(
                new SaturnClickableImage(Textures.SETTINGS, () -> {
                    client.setScreen(new SaturnConfigEditor());
                })
                        .setX(tabsX)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.MODS_TAB, () -> {
                })
                        .setSelected(true)
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

        int modX = 0;
        int modY = 0;
        int col = 0;

        SaturnScroll modsScroll = new SaturnScroll();

        for (SaturnMod mod : ModManager.MODS) {
            modsScroll.draw(
                    new SaturnModComp(mod)
                            .setX(modX)
                            .setY(modY)
                            .setWidth(86)
                            .setHeight(20));

            col++;

            if (col < 3) {
                modX += 86 + 18;
            } else {
                modX = 0;
                modY += 20 + 20;
                col = 0;
            }
        }

        draw(
                modsScroll
                        .setX(modsX + 17)
                        .setY(modsY + 10)
                        .setWidth(rectWidth - 17)
                        .setHeight(rectHeight - 20)
                        .setAnimations(SaturnClient.getAnimations()));

        super.init();
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }
}
