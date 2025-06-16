package org.saturnclient.ui2.screens;

import java.util.Map;

import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.components.inputs.FloatInput;
import org.saturnclient.ui2.components.inputs.HexInput;
import org.saturnclient.ui2.components.inputs.IntInput;
import org.saturnclient.ui2.components.inputs.Select;
import org.saturnclient.ui2.components.inputs.Toggle;
import org.saturnclient.ui2.elements.Scroll;
import org.saturnclient.ui2.elements.Text;
import org.saturnclient.ui2.resources.Fonts;

public class SaturnConfigEditor extends SaturnScreen {
    public SaturnConfigEditor() {
        super("Saturn Config Editor");
    }

    @Override
    public void ui() {
        int p = 10;
        int g = 10;

        Scroll scroll = new Scroll(p);

        drawProperties(scroll, SaturnClientConfig.config.getProperties(), 0, 0, 480 + 10 + (g * 2));

        int scrollWidth = 480 + 10 + (g * 2) + (p * 2);

        draw(scroll.dimensions(scrollWidth, 350).center(width, height));

        draw(new Sidebar(1, this::close).centerOffset(width, height, -(scrollWidth / 2 + 20), 0));
    }

    @SuppressWarnings("unchecked")
    private int drawProperties(Scroll configScroll, Map<String, Property<?>> properties, int row, int col, int w) {
        for (Map.Entry<String, Property<?>> propEntry : properties.entrySet()) {
            Property<?> prop = propEntry.getValue();
            String propName = propEntry.getKey();
            boolean full = isFull(prop);

            // Always reset to col 0 if full-width component needs to be drawn mid-row
            if (full && col != 0) {
                col = 0;
                row++;
            }

            int modX = (w / 2) * col; // two-column layout
            int modY = 25 * row;

            if (prop.getType() == Property.PropertyType.NAMESPACE) {
                configScroll.draw(
                            new Text(
                                    propName
                            ).position(Fonts.centerX(520, propName, Text.font.value), modY + 1).scale(0.7f));
            } else {
                configScroll.draw(
                                new Text(
                                        propName
                                ).position(modX, modY + 1).scale(0.7f));
            }

            switch (prop.getType()) {
                case BOOLEAN:
                    configScroll.draw(
                            new Toggle((Property<Boolean>) prop)
                                    .position(modX + (w / 2) - 40, modY)
                                    .scale(0.5f));
                    break;
                
                case HEX:
                    configScroll.draw(new HexInput((Property<Integer>) prop, this).position(w / 2, modY));
                    break;

                case INTEGER:
                    configScroll.draw(new IntInput((Property<Integer>) prop).position(w / 2, modY));
                    break;

                case FLOAT:
                    configScroll.draw(new FloatInput((Property<Float>) prop).position(w / 2, modY));
                    break;

                case NAMESPACE:
                    Map<String, Property<?>> nestedProperties = (Map<String, Property<?>>) prop.value;
                    row = drawProperties(configScroll, nestedProperties, row + 1, col, w);
                    break;

                case SELECT:
                    configScroll.draw(new Select((Property<Integer>) prop).position(w / 2, modY));

                case STRING:
            }

            if (full) {
                row++; // full-width takes a whole row
                col = 0;
            } else {
                col++;
                if (col > 1) { // max 2 columns
                    col = 0;
                    row++;
                }
            }
        }

        return row;
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
