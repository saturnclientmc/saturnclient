package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.saturnclient.saturnclient.SaturnClient;
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
        int mouseY
    ) {
        context.drawGuiTexture(
            RenderLayer::getGuiTextured,
            sprite,
            this.x,
            this.y,
            this.width,
            this.height,
            ColorHelper.getWhite(this.alpha)
        );
    }
}
