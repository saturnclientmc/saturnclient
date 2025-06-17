package org.saturnclient.modules;

import java.util.Map;

import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.saturnclient.config.Property;

public class ModDimensions {
    public Property<Integer> x = Property.integer(0);
    public Property<Integer> y = Property.integer(0);
    public Property<Float> scale = Property.floatProp(1.0f);
    public Property<Integer> bgColor = Property.color(0x00000000);
    public Property<Integer> fgColor = Property.color(0xFFffffff);
    public Property<Integer> radius = Property.integer(0);
    public Property<Integer> font = Property.font(0);

    public int width = 0;
    public int height = 0;

    public NamedProperty<Map<String, Property<?>>> prop() {
        return Property.namespace(Map.of(
            "X", x,
            "Y", y,
            "Scale", scale,
            "Background Color", bgColor,
            "Foreground Color", fgColor,
            "Corner Radius", radius,
            "Font", font
        )).named("In-Game Display"); 
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
