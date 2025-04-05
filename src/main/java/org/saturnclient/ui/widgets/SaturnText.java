package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnText extends SaturnWidget {

    private String text;

    public SaturnText(String text) {
        this.text = text;
        this.width = SaturnClientConfig.textRenderer.getWidth(SaturnUi.text(text));
        this.height = SaturnClientConfig.textRenderer.fontHeight;
    }

    public SaturnText(String text, int color) {
        this(text);
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        context.drawText(
                SaturnClientConfig.textRenderer,
                SaturnUi.text(text),
                0,
                0,
                SaturnClientConfig.getColor(hovering, alpha),
                false);
    }
}
