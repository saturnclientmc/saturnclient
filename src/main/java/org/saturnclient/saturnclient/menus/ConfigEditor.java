package org.saturnclient.saturnclient.menus;

import java.util.Map;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.Slide;
import org.saturnclient.ui.components.SaturnToggle;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;

import net.minecraft.text.Text;

public class ConfigEditor extends SaturnUi {
    private ConfigManager config;

    public ConfigEditor(ConfigManager config) {
        super(Text.of("Config editor"));
        this.config = config;
    }

    @SuppressWarnings("unchecked")
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
                .setAnimations(new Slide(1, 10)));

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(new SaturnSprite(Textures.TABS)
                .setX(tabsX)
                .setY(tabsY)
                .setWidth(43)
                .setHeight(15)
                .setAnimations(new Slide(1, 10)));

        int modX = 0;
        int modY = 0;
        int col = 0;

        int propWidth = (rectWidth - 15) / 2;

        SaturnScroll modsScroll = new SaturnScroll();

        for (Map.Entry<String, Property<?>> propEntry : config.getProperties().entrySet()) {
            Property<?> prop = propEntry.getValue();
            if (prop.value instanceof Boolean) {
                draw(new SaturnToggle((Property<Boolean>) prop).setX(modX).setY(modY));
            }

            col++;
            if (col < 2) {
                modX += propWidth + 21;
            } else {
                modX = 0;
                modY += 20 + 20;
                col = 0;
            }
        }

        draw(modsScroll.setX(rectX + 17).setY(rectY + 10).setWidth(rectWidth - 17).setHeight(rectHeight - 10));

        super.init();
    }
}
