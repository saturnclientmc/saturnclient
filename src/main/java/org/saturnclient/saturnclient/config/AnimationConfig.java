package org.saturnclient.saturnclient.config;

import org.saturnclient.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.config.manager.Property;

public class AnimationConfig {
    public static ConfigManager config;

    // Animation curve
    public static final Property<Integer> animationCurve = Property.select(1, "Ease In Out", "Ease Out");

    // Logo duration
    public static final Property<Integer> logoDuration = Property.integer(700);

    // Main menu
    public static final AnimationConfig mainMenu = new AnimationConfig(true, 600, 120);

    // Mod menu
    public static final AnimationConfig modMenu = new AnimationConfig(true, 600, 40);

    // Shift menu
    public static final AnimationConfig shiftMenu = new AnimationConfig(true, 300, 50);

    public static void init(ConfigManager parent) {
        config = new ConfigManager(parent, "Animations");

        config.property("Animation Curve", animationCurve);
        config.property("Logo Duration", logoDuration);

        mainMenu.init("Main Menu");
        mainMenu.init("Mod Menu");
        mainMenu.init("Shift Menu");
    }

    // Animation properties
    // public final Property<Boolean> enabled;
    public final Property<Integer> duration;
    public final Property<Integer> stagger;

    public AnimationConfig(boolean enabled, int delay, int stagger) {
        // this.enabled = Property.bool(enabled);
        this.duration = Property.integer(delay);
        this.stagger = Property.integer(stagger);
    }

    public void init(String name) {
        // config.property("Enable " + name + " Animations", this.enabled);
        config.property(name + " Duration", this.duration);
        config.property(name + " Stagger", this.stagger);
    }
}
