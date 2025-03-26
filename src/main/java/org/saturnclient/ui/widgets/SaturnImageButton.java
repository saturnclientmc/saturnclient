package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;

public class SaturnImageButton extends SaturnButton {

    public Identifier sprite;
    public int imageWidth;
    public int imageHeight;
    public Consumer<SaturnImageButton> onPress;

    public SaturnImageButton(
        Identifier sprite,
        int imageWidth,
        int imageHeight,
        Consumer<SaturnImageButton> onPress
    ) {
        super("", _ -> {});
        this.sprite = sprite;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.onPress = onPress;
    }

    public SaturnImageButton(
        Identifier sprite,
        int imageWidth,
        int imageHeight,
        Runnable onPress
    ) {
        this(sprite, imageWidth, imageHeight, _ -> {
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
        super.render(context, hovering, mouseX, mouseY);

        int hoverColor =
            ((int) (alpha * 255) << 24) | (SaturnClient.COLOR & 0x00FFFFFF);

        context.drawTexture(
            RenderLayer::getGuiTextured,
            sprite,
            ((width - imageWidth) / 2),
            ((height - imageHeight) / 2),
            0,
            0,
            imageWidth,
            imageHeight,
            imageWidth,
            imageHeight,
            hovering ? hoverColor : SaturnClient.getWhite(alpha)
        );
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    public SaturnImageButton setBackground(boolean background) {
        this.background = background;
        return this;
    }
}
