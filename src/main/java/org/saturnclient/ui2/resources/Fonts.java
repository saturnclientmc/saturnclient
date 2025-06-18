package org.saturnclient.ui2.resources;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Fonts {
    public static final Identifier INTER = Identifier.of("saturnclient", "inter");
    public static final Identifier INTER_BOLD = Identifier.of("saturnclient", "inter_bold");
    public static final Identifier DEFAULT = Identifier.ofVanilla("default");

    public static Identifier getFont(int font) {
        switch (font) {
            case 0:
                return DEFAULT;
            case 1:
                return INTER;
            default:
                return INTER_BOLD;
        }
    }
    
    public static Text setFont(String text, Identifier font) {
        return Text.literal(text).setStyle(
            Style.EMPTY.withFont(font));
    }

    public static int getWidth(String text, int font) {
        int w = 0;
        for (String line : text.split("\n")) {
            w = Math.max(w, getWidth(line, getFont(font)));
        }
        if (font == 0) {
            return w * 2;
        } else {
            return w;
        }
    }

    private static int getWidth(String text, Identifier font) {
        return SaturnClient.client.textRenderer.getWidth(setFont(text, font));
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
