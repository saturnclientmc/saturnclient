package org.saturnclient.saturnclient.menus;

import net.minecraft.text.Text;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.SaturnMod;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.components.SaturnModComp;
import org.saturnclient.ui.widgets.SaturnClickableImage;
import org.saturnclient.ui.widgets.SaturnInputBox;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

public class ModMenu extends SaturnUi {

    private SaturnScroll scroll;
    private Property<String> search = new Property<>("");

    public ModMenu() {
        super(Text.literal("Mods"));
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
                        .setAnimations(SaturnClientConfig.getAnimations()));

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(
                new SaturnSprite(Textures.RECT)
                        .setX(tabsX)
                        .setY(tabsY)
                        .setWidth(43)
                        .setHeight(15)
                        .setAnimations(SaturnClientConfig.getAnimations()));

        draw(
                new SaturnInputBox(search, rectX + rectWidth - 72, tabsY, 70).onUpdate(() -> {
                    scroll.clear();
                    drawItems(rectX, rectY, rectWidth, rectHeight);
                })
                        .setHeight(15)
                        .setWidth(72)
                        .setAnimations(SaturnClientConfig.getAnimations()));

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
                        .setAnimations(SaturnClientConfig.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.MODS_TAB, () -> {
                })
                        .setSelected(true)
                        .setX(tabsX - 12)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClientConfig.getAnimations()));

        draw(
                new SaturnClickableImage(Textures.SEARCH, () -> {
                    System.out.println("Pressed");
                })
                        .setX(tabsX + 12)
                        .setY(tabsY + 3)
                        .setWidth(tabSize)
                        .setHeight(tabSize)
                        .setAnimations(SaturnClientConfig.getAnimations()));

        scroll = new SaturnScroll();

        drawItems(rectX, rectY, rectWidth, rectHeight);

        draw(scroll
                .setX(rectX + 17)
                .setY(rectY + 10)
                .setWidth(rectWidth - 17)
                .setHeight(rectHeight - 20)
                .setAnimations(SaturnClientConfig.getAnimations()));

        super.init();
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }

    private void drawItems(int rectX, int rectY, int rectWidth, int rectHeight) {
        int modX = 0;
        int modY = 0;
        int col = 0;

        String[] searchTokens = search.value.toLowerCase().split("\\ ");

        outer: for (SaturnMod mod : ModManager.MODS) {
            String modName = mod.getName().toLowerCase();
            for (String searchToken : searchTokens) {
                if (!modName.contains(searchToken)) {
                    continue outer;
                }
            }

            scroll.draw(
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
    }
}
