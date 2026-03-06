package org.saturnclient.ui.resources;

import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;

public class Fonts {
    public static final SaturnIdentifier INTER = SaturnIdentifier.of("saturnclient", "inter");
    public static final SaturnIdentifier INTER_BOLD = SaturnIdentifier.of("saturnclient", "inter_bold");
    public static final SaturnIdentifier DEFAULT = SaturnIdentifier.ofVanilla("default");

    public static SaturnIdentifier getFont(int font) {
        switch (font) {
            case 0:
                return DEFAULT;
            case 1:
                return INTER;
            default:
                return INTER_BOLD;
        }
    }

    public static int getWidth(String text, int font) {
        int w = 0;
        for (String line : text.split("\n")) {
            w = Math.max(w, MinecraftProvider.PROVIDER.getWidth(line, font));
        }
        if (font == 0) {
            return w * 2;
        } else {
            return w;
        }
    }

    public static int getHeight() {
        return 18;
    }

    public static int centerX(int w, String text, int font) {
        return (w - Fonts.getWidth(text, font)) / 2;
    }

    public static int centerY(int h) {
        return (h - 18) / 2;
    }
}
