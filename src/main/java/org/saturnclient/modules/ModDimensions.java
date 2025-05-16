package org.saturnclient.modules;

import java.util.Map;

import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.saturnclient.config.Property;

public class ModDimensions {
    public Property<Integer> x = new Property<>(0);
    public Property<Integer> y = new Property<>(0);
    public Property<Float> scale = new Property<>(1.0f);
    public Property<Integer> bgColor = new Property<>(0xFF000000, Property.PropertyType.HEX);
    public Property<Integer> fgColor = new Property<>(0xFFffffff, Property.PropertyType.HEX);
    public Property<Integer> radius = new Property<>(0);

    public int width = 0;
    public int height = 0;

    public NamedProperty<Map<String, Property<?>>> prop() {
        return new Property<Map<String, Property<?>>>(Map.of(
            "X", x,
            "Y", y,
            "Scale", scale,
            "Background Color", bgColor,
            "Foreground Color", fgColor,
            "Corner Radius", radius
        )).named("Hud Position"); 
    }

    public ModDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ModDimensions(int x, int y, int width, int height) {
        this.x.value = x;
        this.y.value = y;
        this.width = width;
        this.height = height;
    }

    public ModDimensions(int x, int y, int width, int height, float scale) {
        this.x.value = x;
        this.y.value = y;
        this.scale.value = scale;
        this.width = width;
        this.height = height;
    }
}
