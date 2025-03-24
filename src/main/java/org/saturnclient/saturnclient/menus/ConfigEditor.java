package org.saturnclient.saturnclient.menus;

import java.util.Map;
import net.minecraft.text.Text;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.Slide;
import org.saturnclient.ui.components.SaturnToggle;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;
import org.saturnclient.ui.widgets.SaturnText;

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

        draw(
            new SaturnSprite(Textures.SETTINGS_BG)
                .setX(rectX)
                .setY(rectY)
                .setWidth(rectWidth)
                .setHeight(rectHeight)
                .setAnimations(new Slide(1, 10))
        );

        int tabsX = (width - 43) / 2;
        int tabsY = rectY - 17;

        draw(
            new SaturnSprite(Textures.TABS)
                .setX(tabsX)
                .setY(tabsY)
                .setWidth(43)
                .setHeight(15)
                .setAnimations(new Slide(1, 10))
        );

        int propWidth = (rectWidth - 15) / 2;

        SaturnScroll configScroll = new SaturnScroll();

        int row = 0;
        int col = 0;

        for (Map.Entry<String, Property<?>> propEntry : config
            .getProperties()
            .entrySet()) {
            Property<?> prop = propEntry.getValue();
            String propName = propEntry.getKey();

            boolean full = isFull(prop);

            if (full && col > 0) {
                col = 0;
                row++;
            }

            int modX = ((rectWidth / 2) - 17) * col;
            int modY = 12 * row;

            switch (prop.getType()) {
                case BOOLEAN:
                    configScroll.draw(
                        new SaturnToggle((Property<Boolean>) prop)
                            .setX(modX)
                            .setY(modY)
                    );
                    configScroll.draw(
                        new SaturnText(
                            propName.substring(0, 1).toUpperCase() +
                            propName.substring(1)
                        )
                            .setX(modX + 18)
                            .setY(modY)
                    );
                    break;
                case INTEGER:
                    configScroll.draw(
                        new SaturnText(
                            propName.substring(0, 1).toUpperCase() +
                            propName.substring(1)
                        )
                            .setX(modX)
                            .setY(modY)
                    );

                    configScroll.draw(
                        new SaturnText("" + prop.value)
                            .setX((rectWidth / 2) - 17)
                            .setY(modY)
                    );
                    break;
            }

            if (full) {
                row++;
            } else {
                col++;
                if (col > 1) {
                    col = 0;
                    row++;
                }
            }
        }

        draw(
            configScroll
                .setX(rectX + 17)
                .setY(rectY + 10)
                .setWidth(rectWidth - 17)
                .setHeight(rectHeight - 10)
        );

        super.init();
    }

    public static boolean isFull(Property<?> prop) {
        switch (prop.getType()) {
            case BOOLEAN:
                return false;
            default:
                return true;
        }
    }
}
