package org.saturnclient.saturnclient.menus;

import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.cloaks.Cloaks;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.FadeIn;
import org.saturnclient.ui.widgets.SaturnClickableSprite;
import org.saturnclient.ui.widgets.SaturnSprite;
import org.saturnclient.ui.widgets.SaturnImage;

import net.minecraft.text.Text;

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

        draw(new SaturnSprite(Textures.SETTINGS_BG)
                .setX(rectX)
                .setY(rectY)
                .setWidth(rectWidth)
                .setHeight(rectHeight)
                .setAnimations(new FadeIn(1)));

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(new SaturnSprite(Textures.TABS)
                .setX(tabsX)
                .setY(tabsY)
                .setWidth(43)
                .setHeight(15)
                .setAnimations(new FadeIn(1)));

        int cloakWidth = 32;
        int cloakHeight = 70;

        int modX = rectX + 15;
        int modY = rectY + 10;
        int col = 0;

        for (String cloak : Cloaks.availableCloaks) {
            draw(new SaturnImage(Textures.getCloakPreview(cloak))
                    .setX(modX)
                    .setY(modY)
                    .setWidth(cloakWidth)
                    .setHeight(cloakHeight));

            draw(new SaturnClickableSprite(Textures.BUTTON_BORDER, () -> {
                Cloaks.setCloak(SaturnSocket.uuid, cloak);
            })
                    .setColor(10526880)
                    .setX(modX - 3)
                    .setY(modY - 3)
                    .setWidth(cloakWidth + 6)
                    .setHeight(cloakHeight + 6));

            col++;

            if (col < 6) {
                modX += cloakWidth + 21;
            } else {
                modX = rectX + 15;
                modY += cloakHeight + 20;
                col = 0;
            }
        }
    }
}
