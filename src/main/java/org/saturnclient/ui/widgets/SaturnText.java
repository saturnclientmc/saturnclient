package org.saturnclient.ui.widgets;

import org.saturnclient.ui.SaturnWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;

public class SaturnText extends SaturnWidget {
    private String text;

    public SaturnText(String text) {
        this.text = text;
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, ColorHelper.getWhite(alpha), false);
    }
}
