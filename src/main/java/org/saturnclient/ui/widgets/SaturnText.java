package org.saturnclient.ui.widgets;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;

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
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.drawText(SaturnClient.textRenderer,
                SaturnUi.text(text), x, y,
                ColorHelper.getWhite(alpha), false);
    }
}
