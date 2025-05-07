package org.saturnclient.ui2.resources;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Fonts {
    public static final Identifier INTER = Identifier.of("saturnclient", "inter");
    public static final Identifier INTER_BOLD = Identifier.of("saturnclient", "inter_bold");

    public static Identifier getFont(boolean bold) {
        if (bold) {
            return INTER_BOLD;
        } else {
            return INTER;
        }
    }
    
    public static Text setFont(String text, Identifier font) {
        return Text.literal(text).setStyle(
            Style.EMPTY.withFont(font));
    }

    // public static int getWidth(String text, Identifier font) {
    //     return SaturnClient.client.textRenderer.getWidth(Text.literal(text).setStyle(
    //         Style.EMPTY.withFont(font)));
    // }

    // public static int getHeight() {
    //     return SaturnClient.client.textRenderer.fontHeight;
    // }

    public static int getWidth(String text, boolean bold) {
        return SaturnClient.client.textRenderer.getWidth(setFont(text, getFont(bold)));
    }

    public static int getHeight() {
        return 18;
    }

    public static int centerX(int w, String text, boolean bold) {
        return (w - Fonts.getWidth(text, bold)) / 2;
    }

    public static int centerY(int h) {
        return (h - 18) / 2;
    }
}
