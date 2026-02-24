package org.saturnclient.saturnclient.config;

import org.saturnclient.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.config.manager.Property;

public class AnimationConfig {
    public static ConfigManager config;

    // Logo duration
    public static final Property<Integer> logoDuration = Property.integer(700);

    // Main menu
    public static final AnimationConfig mainMenu = new AnimationConfig(true, 700, 120);

    // Mod menu
    public static final AnimationConfig modMenu = new AnimationConfig(true, 700, 30);

    // Shift menu
    public static final AnimationConfig shiftMenu = new AnimationConfig(true, 300, 120);

    public static void init(ConfigManager parent) {
        config = new ConfigManager(parent, "Animations");
        config.property("Logo Duration", logoDuration);

        mainMenu.init("Main Menu");
        modMenu.init("Mod Menu");
        shiftMenu.init("Shift Menu");
    }

    // Animation properties
    public final Property<Integer> duration;
    public final Property<Integer> stagger;
    public final Property<Integer> curve;

    public AnimationConfig(boolean enabled, int delay, int stagger) {
        this.duration = Property.integer(delay);
        this.stagger = Property.integer(stagger);
        this.curve = Property.select(1, "Ease In Out Cubic", "Ease Out Cubic", "Ease In Out Back");
    }

    public void init(String name) {
        // config.property("Enable " + name + " Animations", this.enabled);
        config.property(name + " Duration", this.duration);
        config.property(name + " Stagger", this.stagger);
    }
}
