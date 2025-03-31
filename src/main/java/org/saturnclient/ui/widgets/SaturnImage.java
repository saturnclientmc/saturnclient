package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnImage extends SaturnWidget {

    private final Identifier sprite;

    public SaturnImage(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        SaturnUi.drawHighResTexture(
                context,
                sprite,
                0,
                0,
                width,
                height,
                ColorHelper.getWhite(alpha));
    }
}
