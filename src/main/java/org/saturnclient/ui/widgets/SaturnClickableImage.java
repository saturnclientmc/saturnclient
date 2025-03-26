package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnWidget;

public class SaturnClickableImage extends SaturnWidget {

    public Identifier texture;
    public boolean selected = false;
    public Consumer<SaturnClickableImage> onPress;
    public int color = SaturnClient.getWhite(1.0f);
    public int hoverColor = SaturnClient.COLOR;

    public SaturnClickableImage(
        Identifier texture,
        Consumer<SaturnClickableImage> onPress
    ) {
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
        int mouseY
    ) {
        context.drawTexture(
            RenderLayer::getGuiTextured,
            texture,
            0,
            0,
            0,
            0,
            width,
            height,
            width,
            height,
            (hovering || selected ? hoverColor : color) |
            (MathHelper.ceil(this.alpha * 255.0F) << 24)
        );
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    public SaturnClickableImage setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
