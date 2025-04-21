package org.saturnclient.ui2.resources;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Fonts {
    public static final Identifier PANTON = Identifier.of("saturnclient", "panton");
    public static final Identifier PANTON_BOLD = Identifier.of("saturnclient", "panton_bold");

    public static Identifier getFont(boolean bold) {
        if (bold) {
            return PANTON_BOLD;
        } else {
            return PANTON;
        }
    }
    
    public static Text setFont(String text, Identifier font) {
        return Text.literal(text).setStyle(
            Style.EMPTY.withFont(font));
    }

    public static int getWidth(String text, Identifier font) {
        return MinecraftClient.getInstance().textRenderer.getWidth(Text.literal(text).setStyle(
            Style.EMPTY.withFont(font)));
    }

    public static int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }
}
