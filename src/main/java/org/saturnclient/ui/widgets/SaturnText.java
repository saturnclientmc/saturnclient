package org.saturnclient.ui.widgets;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class SaturnText extends SaturnWidget {
    private String text;
    private int color = ColorHelper.getWhite(1.0f);

    public SaturnText(String text) {
        this.text = text;
        this.width = SaturnClient.textRenderer.getWidth(SaturnUi.text(text));
        this.height = SaturnClient.textRenderer.fontHeight;
    }

    public SaturnText(String text, int color) {
        this(text);
        this.color = color;
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.drawText(SaturnClient.textRenderer,
                SaturnUi.text(text), x, y,
                color | MathHelper.ceil(this.alpha * 255.0F) << 24, false);
    }
}
