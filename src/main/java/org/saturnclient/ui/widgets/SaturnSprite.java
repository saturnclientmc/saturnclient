package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnSprite extends SaturnWidget {

    private final Identifier sprite;

    public SaturnSprite(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        SaturnUi.drawHighResGuiTexture(
                context,
                sprite,
                0,
                0,
                this.width,
                this.height,
                ColorHelper.getWhite(this.alpha));
    }
}
