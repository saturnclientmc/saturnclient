package org.saturnclient.saturnclient.menus;

import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnClickableSprite;
import org.saturnclient.ui.widgets.SaturnImage;
import org.saturnclient.ui.widgets.SaturnInputBox;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

public class CloaksMenu extends SaturnUi {

    private SaturnScroll scroll;
    private Property<String> search = new Property<>("");

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
                        .setAnimations(SaturnClient.getAnimations()));

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(
                new SaturnSprite(Textures.RECT)
                        .setX(tabsX)
                        .setY(tabsY)
                        .setWidth(43)
                        .setHeight(15)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnInputBox(search, rectX + rectWidth - 72, tabsY, 70).onUpdate(() -> {
                    scroll.clear();
                    drawItems(rectX, rectY, rectWidth, rectHeight);
                })
                        .setHeight(15)
                        .setWidth(72)
                        .setAnimations(SaturnClient.getAnimations()));

        int tabSize = 9;
        tabsX = (width - tabSize) / 2;
        draw(
                new SaturnClickableImage(Textures.HAT, () -> {
                    client.setScreen(new HatsMenu());
                })
                        .setX(tabsX)
                        .setY(tabsY + 4)
                        .setWidth(tabSize)
                        .setHeight(7)
                        .setAnimations(SaturnClient.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.CLOAK, () -> {
                    client.setScreen(new CloaksMenu());
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

        scroll = new SaturnScroll();

        drawItems(rectX, rectY, rectWidth, rectHeight);

        draw(scroll
                .setX(rectX + 17)
                .setY(rectY + 10)
                .setWidth(rectWidth - 17)
                .setHeight(rectHeight - 10)
                .setAnimations(SaturnClient.getAnimations()));

        super.init();
    }

    private void drawItems(int rectX, int rectY, int rectWidth, int rectHeight) {
        int cloakWidth = 32;
        int cloakHeight = 70;

        int modX = 0;
        int modY = 0;
        int col = 0;

        String[] searchTokens = search.value.split("\\ ");

        outer: for (String cloak : Cloaks.availableCloaks) {
            for (String searchToken : searchTokens) {
                if (!cloak.contains(searchToken)) {
                    continue outer;
                }
            }

            scroll.draw(
                    new SaturnClickableSprite(Textures.BUTTON_BORDER, () -> {
                        Cloaks.setCloak(SaturnSocket.uuid, cloak);
                    })
                            .setX(modX)
                            .setY(modY)
                            .setWidth(cloakWidth + 6)
                            .setHeight(cloakHeight + 6));

            scroll.draw(
                    new SaturnImage(Textures.getCloakPreview(cloak))
                            .setX(modX + 3)
                            .setY(modY + 3)
                            .setWidth(cloakWidth)
                            .setHeight(cloakHeight));

            col++;

            if (col < 6) {
                modX += cloakWidth + 21;
            } else {
                modX = 0;
                modY += cloakHeight + 20;
                col = 0;
            }
        }
    }
}
