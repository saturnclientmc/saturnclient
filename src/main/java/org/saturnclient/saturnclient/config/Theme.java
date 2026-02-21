package org.saturnclient.saturnclient.config;

public class Theme {
    // Main UI background (slightly warm dark to match Saturn tone)
    public static Property<Integer> BACKGROUND = Property.color(0xFF12100D);

    // Default text color
    public static Property<Integer> FOREGROUND = Property.color(0xFFF2E6D2);

    // Widget default background (normal state)
    public static Property<Integer> PRIMARY = Property.color(0xFF1C1915);

    // Widget text color
    public static Property<Integer> PRIMARY_FG = Property.color(0xFFEAD9B8);

    // Saturn brand color (hovered / enabled / selected)
    public static Property<Integer> ACCENT = Property.color(0xFFE2BF7D);

    // Text/icons shown on accent background
    public static Property<Integer> ACCENT_FG = Property.color(0xFF1A140A);

    // UI metrics
    public static Property<Integer> BG_RADIUS = Property.integer(12);
    public static Property<Integer> WIDGET_RADIUS = Property.integer(10);
    public static Property<Integer> PADDING = Property.integer(14);

    // Font (keeping your current system)
    public static Property<Integer> FONT = Property.font(1);

    public static void init(ConfigManager parent) {
        ConfigManager config = new ConfigManager(parent, "UI Style");
        config.property("Background", BACKGROUND);
        config.property("Foreground", FOREGROUND);
        config.property("Primary", PRIMARY);
        config.property("Primary Foreground", PRIMARY_FG);
        config.property("Accent", ACCENT);
        config.property("Accent Foreground", ACCENT_FG);
        config.property("Background Radius", BG_RADIUS);
        config.property("Widget Radius", WIDGET_RADIUS);
        config.property("Font", WIDGET_RADIUS);
    }
}
