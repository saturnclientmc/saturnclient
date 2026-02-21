package org.saturnclient.saturnclient.config;

public class Theme {
    public static Property<Integer> BACKGROUND = Property.color(0x00000000);
    public static Property<Integer> FOREGROUND = Property.color(0x00000000);
    public static Property<Integer> PRIMARY = Property.color(0x00000000);
    public static Property<Integer> PRIMARY_FG = Property.color(0x00000000);
    public static Property<Integer> ACCENT = Property.color(0x00000000);
    public static Property<Integer> ACCENT_FG = Property.color(0x00000000);
    public static Property<Integer> BG_RADIUS = Property.integer(10);
    public static Property<Integer> WIDGET_RADIUS = Property.integer(10);
    public static Property<Integer> PADDING = Property.integer(15);

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
    }
}
