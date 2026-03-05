package org.saturnclient.saturnclient.config;

import org.saturnclient.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.config.manager.Property;

public class Theme {
    // Main UI background (slightly warm dark to match Saturn tone)
    public static final Property<Integer> BACKGROUND = Property.color(0xFF12100D);

    // Default text color
    public static final Property<Integer> FOREGROUND = Property.color(0xFFF2E6D2);

    // Widget default background (normal state)
    public static final Property<Integer> PRIMARY = Property.color(0xFF1C1915);

    // Widget text color
    public static final Property<Integer> PRIMARY_FG = Property.color(0xFFEAD9B8);

    // Saturn brand color (hovered / enabled / selected)
    public static final Property<Integer> ACCENT = Property.color(0xFFE2BF7D);

    // Text/icons shown on accent background
    public static final Property<Integer> ACCENT_FG = Property.color(0xFF1A140A);

    // Misc colors
    public static final Property<Integer> SCROLL = Property.color(0xFFE2BF7D);

    // UI metrics
    public static final Property<Integer> BG_RADIUS = Property.integer(12);
    public static final Property<Integer> WIDGET_RADIUS = Property.integer(10);

    // Font
    public static final Property<Integer> FONT = Property.font(2);

    public static void init(ConfigManager parent) {
        ConfigManager config = new ConfigManager(parent, "UI Style");
        config.property("Background", BACKGROUND);
        config.property("Foreground", FOREGROUND);
        config.property("Primary", PRIMARY);
        config.property("Primary Foreground", PRIMARY_FG);
        config.property("Accent", ACCENT);
        config.property("Accent Foreground", ACCENT_FG);
        config.property("Scrollbar Color", SCROLL);

        config.property("Background Radius", BG_RADIUS);
        config.property("Widget Radius", WIDGET_RADIUS);

        config.property("Font", FONT);
    }

    /**
     * primary by default, accent if true
     */
    public static int getBg(boolean hover) {
        return hover ? ACCENT.value : PRIMARY.value;
    }

    /**
     * (Foreground) primary by default, accent if true
     */
    public static int getFg(boolean hover) {
        return hover ? ACCENT_FG.value : PRIMARY_FG.value;
    }

    /**
     * Multiplies existing alpha with 0–255 alpha.
     */
    public static int withAlpha(int alpha, int color) {
        alpha = Math.max(0, Math.min(255, alpha));

        int originalAlpha = (color >>> 24) & 0xFF;

        // Merge (multiply) alphas
        int merged = (originalAlpha * alpha) / 255;

        return (color & 0x00FFFFFF) | (merged << 24);
    }

    /**
     * Multiplies existing alpha with 0.0–1.0 alpha.
     */
    public static int withAlpha(float alpha, int color) {
        alpha = Math.max(0f, Math.min(1f, alpha));

        int originalAlpha = (color >>> 24) & 0xFF;

        int merged = (int) (originalAlpha * alpha);

        return (color & 0x00FFFFFF) | (merged << 24);
    }
}
