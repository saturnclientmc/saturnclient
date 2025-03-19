package org.saturnclient.saturnmods;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class ModDimensions {
    public Property<Integer> x = new Property<>(0);
    public Property<Integer> y = new Property<>(0);
    public Property<Float> scale = new Property<>(1.0f);

    public int width = 0;
    public int height = 0;

    public ModDimensions(ConfigManager configManager) {
        configManager.property("x", this.x);
        configManager.property("y", this.y);
        configManager.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y) {
        this.x.value = x;
        this.y.value = y;
        configManager.property("x", this.x);
        configManager.property("y", this.y);
        configManager.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, int width, int height) {
        this.x.value = x;
        this.y.value = y;
        this.width = width;
        this.height = height;
        configManager.property("x", this.x);
        configManager.property("y", this.y);
        configManager.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, float scale) {
        this.x.value = x;
        this.y.value = y;
        this.scale.value = scale;
        configManager.property("x", this.x);
        configManager.property("y", this.y);
        configManager.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, int width, int height, float scale) {
        this.x.value = x;
        this.y.value = y;
        this.scale.value = scale;
        this.width = width;
        this.height = height;
        configManager.property("x", this.x);
        configManager.property("y", this.y);
        configManager.property("scale", this.scale);
    }
}
