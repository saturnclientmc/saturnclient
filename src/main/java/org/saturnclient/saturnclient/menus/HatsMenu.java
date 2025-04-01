package org.saturnclient.saturnclient.menus;

import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnClickableSprite;
import org.saturnclient.ui.widgets.SaturnImage;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

public class HatsMenu extends SaturnUi {

    public HatsMenu() {
        super(Text.of("Hats Menu"));
    }

    @Override
    protected void init() {
        int rectWidth = 330;
        int rectHeight = 228;
        int rectX = (width - rectWidth) / 2;
        int rectY = (height - rectHeight + 15) / 2;

        draw(
                new SaturnSprite(Textures.SETTINGS_BG)
                        .setX(rectX)
                        .setY(rectY)
                        .setWidth(rectWidth)
                        .setHeight(rectHeight)
                        .setAnimations(SaturnClient.getAnimations()));

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

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
                new SaturnClickableImage(Textures.HAT, () -> {
                    client.setScreen(new HatsMenu());
                })
                        .setColor(SaturnClient.COLOR.value)
                        .setX(tabsX)
                        .setY(tabsY + 4)
                        .setWidth(tabSize)
                        .setHeight(7)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.CLOAK, () -> {
                    client.setScreen(new CloaksMenu());
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

        int hatSize = 32;

        int modX = 0;
        int modY = 0;
        int col = 0;

        SaturnScroll modsScroll = new SaturnScroll();

        for (String hat : Hats.availableHats) {
            modsScroll.draw(
                    new SaturnClickableSprite(Textures.BUTTON_BORDER, () -> {
                        // Hats.setHat(SaturnSocket.uuid, hat);
                    })
                            .setColor(10526880)
                            .setX(modX)
                            .setY(modY)
                            .setWidth(hatSize + 6)
                            .setHeight(hatSize + 6)
                            .setAnimations(SaturnClient.getAnimations()));

            modsScroll.draw(
                    new SaturnImage(Textures.getHatPreview(hat))
                            .setX(modX + 3)
                            .setY(modY + 3)
                            .setWidth(hatSize)
                            .setHeight(hatSize)
                            .setAnimations(SaturnClient.getAnimations()));

            col++;

            if (col < 6) {
                modX += hatSize + 21;
            } else {
                modX = 0;
                modY += hatSize + 20;
                col = 0;
            }
        }

        draw(
                modsScroll
                        .setX(rectX + 17)
                        .setY(rectY + 10)
                        .setWidth(rectWidth - 17)
                        .setHeight(rectHeight - 10)
                        .setAnimations(SaturnClient.getAnimations()));

        super.init();
    }
}
