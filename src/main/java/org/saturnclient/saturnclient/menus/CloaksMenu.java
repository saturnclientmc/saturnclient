package org.saturnclient.saturnclient.menus;

import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnClickableSprite;
import org.saturnclient.ui.widgets.SaturnImage;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

public class CloaksMenu extends SaturnUi {

    public CloaksMenu() {
        super(Text.of("Cloaks Menu"));
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
                .setAnimations(SaturnClient.getAnimations())
        );

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(
            new SaturnSprite(Textures.TABS)
                .setX(tabsX)
                .setY(tabsY)
                .setWidth(43)
                .setHeight(15)
                .setAnimations(SaturnClient.getAnimations())
        );

        int cloakWidth = 32;
        int cloakHeight = 70;

        int modX = 0;
        int modY = 0;
        int col = 0;

        SaturnScroll modsScroll = new SaturnScroll();

        for (String cloak : Cloaks.availableCloaks) {
            modsScroll.draw(
                new SaturnClickableSprite(Textures.BUTTON_BORDER, () -> {
                    Cloaks.setCloak(SaturnSocket.uuid, cloak);
                })
                    .setColor(10526880)
                    .setX(modX)
                    .setY(modY)
                    .setWidth(cloakWidth + 6)
                    .setHeight(cloakHeight + 6)
            );

            modsScroll.draw(
                new SaturnImage(Textures.getCloakPreview(cloak))
                    .setX(modX + 3)
                    .setY(modY + 3)
                    .setWidth(cloakWidth)
                    .setHeight(cloakHeight)
            );

            col++;

            if (col < 6) {
                modX += cloakWidth + 21;
            } else {
                modX = 0;
                modY += cloakHeight + 20;
                col = 0;
            }
        }

        draw(
            modsScroll
                .setX(rectX + 17)
                .setY(rectY + 10)
                .setWidth(rectWidth - 17)
                .setHeight(rectHeight - 10)
                .setAnimations(SaturnClient.getAnimations())
        );

        super.init();
    }
}
