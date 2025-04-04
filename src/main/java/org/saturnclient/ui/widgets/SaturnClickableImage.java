package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnClickableImage extends SaturnWidget {

    public Identifier texture;
    public boolean selected = false;
    public Consumer<SaturnClickableImage> onPress;
    private float hoverAlpha = 0.0f;

    public SaturnClickableImage(
            Identifier texture,
            Consumer<SaturnClickableImage> onPress) {
        this.texture = texture;
        this.onPress = onPress;
    }

    public SaturnClickableImage(Identifier texture, Runnable onPress) {
        this(texture, _o -> {
            onPress.run();
        });
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        if (hovering) {
            if (hoverAlpha == 0.0f)
                hoverAlpha = 0.1f;
        }

        SaturnUi.drawHighResTexture(
                context,
                texture,
                0,
                0,
                width,
                height,
                SaturnClient.getColor(hovering || selected, alpha));
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    public SaturnClickableImage setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void tick() {
        if (hoverAlpha > 0.0f && hoverAlpha < 0.9f)
            hoverAlpha += 0.03f;
        else if (hoverAlpha > 0.9f && hoverAlpha < 1.0f)
            hoverAlpha = 1.0f;
    }
}
