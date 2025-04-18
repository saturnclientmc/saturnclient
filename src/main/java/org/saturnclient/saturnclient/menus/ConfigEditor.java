package org.saturnclient.saturnclient.menus;

import java.util.Map;
import net.minecraft.text.Text;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.components.SaturnFloat;
import org.saturnclient.ui.components.SaturnHex;
import org.saturnclient.ui.components.SaturnInteger;
import org.saturnclient.ui.components.SaturnString;
import org.saturnclient.ui.components.SaturnToggle;
import org.saturnclient.ui.widgets.SaturnImageButton;
import org.saturnclient.ui.widgets.SaturnScroll;
import org.saturnclient.ui.widgets.SaturnSprite;
import org.saturnclient.ui.widgets.SaturnText;

public class ConfigEditor extends SaturnUi {

    private ConfigManager config;

    public ConfigEditor(ConfigManager config) {
        super(Text.of("Config editor"));
        this.config = config;
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

        draw(
                new SaturnImageButton(Textures.CLOSE, 9, 9, () -> {
                    this.close();
                })
                        .setX(rectX)
                        .setY(rectY - 15)
                        .setWidth(13)
                        .setHeight(13)
                        .setAnimations(SaturnClientConfig.getAnimations()));

        SaturnScroll configScroll = new SaturnScroll();

        int row = 0;
        int col = 0;

        drawProperties(configScroll, config.getProperties(), rectWidth, row, col);

        draw(
                configScroll
                        .setX(rectX + 17)
                        .setY(rectY + 10)
                        .setWidth(rectWidth - 17)
                        .setHeight(rectHeight - 10)
                        .setAnimations(SaturnClientConfig.getAnimations()));

        super.init();
    }

    @SuppressWarnings("unchecked")
    private int drawProperties(SaturnScroll configScroll, Map<String, Property<?>> properties, int rectWidth,
            int row, int col) {
        int totalRows = row;
        for (Map.Entry<String, Property<?>> propEntry : properties.entrySet()) {
            Property<?> prop = propEntry.getValue();
            String propName = propEntry.getKey();

            boolean full = isFull(prop);

            if (full && col > 0) {
                col = 0;
                totalRows++;
            }

            int modX = ((rectWidth / 2) - 17) * col;
            int modY = 14 * totalRows;

            switch (prop.getType()) {
                case BOOLEAN:
                    configScroll.draw(
                            new SaturnToggle((Property<Boolean>) prop)
                                    .setX(modX)
                                    .setY(modY));
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX + 18)
                                    .setY(modY)
                                    .setScale(0.8f));
                    break;
                case INTEGER:
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX)
                                    .setY(modY)
                                    .setScale(0.8f));

                    configScroll.draw(
                            new SaturnInteger(
                                    (Property<Integer>) prop,
                                    (rectWidth / 2) - 17,
                                    modY,
                                    70));

                    configScroll.draw(resetButton(prop, modX, modY));
                    break;
                case FLOAT:
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX)
                                    .setY(modY)
                                    .setScale(0.8f));

                    configScroll.draw(
                            new SaturnFloat(
                                    (Property<Float>) prop,
                                    (rectWidth / 2) - 17,
                                    modY,
                                    70));

                    configScroll.draw(resetButton(prop, modX, modY));
                    break;
                case STRING:
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX)
                                    .setY(modY)
                                    .setScale(0.8f));

                    configScroll.draw(
                            new SaturnString(
                                    (Property<String>) prop,
                                    (rectWidth / 2) - 17,
                                    modY,
                                    70));

                    configScroll.draw(resetButton(prop, modX, modY));
                    break;
                case HEX:
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX)
                                    .setY(modY)
                                    .setScale(0.8f));

                    configScroll.draw(
                            new SaturnHex(
                                    (Property<Integer>) prop,
                                    (rectWidth / 2) - 17,
                                    modY,
                                    70));

                    configScroll.draw(resetButton(prop, modX, modY));
                    break;
                case NAMESPACE:
                    configScroll.draw(
                            new SaturnText(
                                    propName.substring(0, 1).toUpperCase() +
                                            propName.substring(1))
                                    .setX(modX)
                                    .setY(modY)
                                    .setScale(0.8f));

                    // Recursively draw nested properties and update total rows
                    Map<String, Property<?>> nestedProperties = (Map<String, Property<?>>) prop.value;
                    totalRows = drawProperties(configScroll, nestedProperties, rectWidth,
                            totalRows + 1, 0);
            }

            if (full) {
                totalRows++;
            } else {
                col++;
                if (col > 1) {
                    col = 0;
                    totalRows++;
                }
            }
        }
        return totalRows;
    }

    public static boolean isFull(Property<?> prop) {
        switch (prop.getType()) {
            case BOOLEAN:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }

    public SaturnWidget resetButton(Property<?> prop, int modX, int modY) {
        int size = textRenderer.fontHeight - 1;
        return new SaturnImageButton(
                Textures.RESET,
                size,
                size,
                () -> {
                    prop.reset();
                })
                .setX(modX + 209)
                .setY(modY)
                .setWidth(size + 3)
                .setHeight(size + 3);
    }
}
