package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnText extends SaturnWidget {

    private String text;

    public SaturnText(String text) {
        this.text = text;
        this.width = SaturnClient.textRenderer.getWidth(SaturnUi.text(text));
        this.height = SaturnClient.textRenderer.fontHeight;
    }

    public SaturnText(String text, int color) {
        this(text);
    }

    @Override
    public void render(
        DrawContext context,
        boolean hovering,
        int mouseX,
        int mouseY
    ) {
        context.drawText(
            SaturnClient.textRenderer,
            SaturnUi.text(text),
            0,
            0,
            SaturnClient.getWhite(alpha),
            false
        );
    }
}
