package org.saturnclient.saturnmods;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class ModDimensions {
    public Property<Integer> x = new Property<>(0);
    public Property<Integer> y = new Property<>(0);
    public Property<Float> scale = new Property<>(1.0f);

    public int width = 0;
    public int height = 0;

    private final ConfigManager dimensionsConfig;

    public ModDimensions(ConfigManager configManager) {
        dimensionsConfig = new ConfigManager(configManager, "Dimensions");
        dimensionsConfig.property("x", this.x);
        dimensionsConfig.property("y", this.y);
        dimensionsConfig.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y) {
        dimensionsConfig = new ConfigManager(configManager, "Dimensions");
        this.x.value = x;
        this.y.value = y;
        dimensionsConfig.property("x", this.x);
        dimensionsConfig.property("y", this.y);
        dimensionsConfig.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, int width, int height) {
        dimensionsConfig = new ConfigManager(configManager, "Dimensions");
        this.x.value = x;
        this.y.value = y;
        this.width = width;
        this.height = height;
        dimensionsConfig.property("x", this.x);
        dimensionsConfig.property("y", this.y);
        dimensionsConfig.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, float scale) {
        dimensionsConfig = new ConfigManager(configManager, "Dimensions");
        this.x.value = x;
        this.y.value = y;
        this.scale.value = scale;
        dimensionsConfig.property("x", this.x);
        dimensionsConfig.property("y", this.y);
        dimensionsConfig.property("scale", this.scale);
    }

    public ModDimensions(ConfigManager configManager, int x, int y, int width, int height, float scale) {
        dimensionsConfig = new ConfigManager(configManager, "Dimensions");
        this.x.value = x;
        this.y.value = y;
        this.scale.value = scale;
        this.width = width;
        this.height = height;
        dimensionsConfig.property("x", this.x);
        dimensionsConfig.property("y", this.y);
        dimensionsConfig.property("scale", this.scale);
    }
}
