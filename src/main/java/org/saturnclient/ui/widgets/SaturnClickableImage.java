package org.saturnclient.ui.widgets;

import java.util.function.Consumer;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnWidget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnClickableImage extends SaturnWidget {
    public Identifier sprite;
    public boolean selected = false;
    public Consumer<SaturnClickableImage> onPress;

    public SaturnClickableImage(Identifier sprite, Consumer<SaturnClickableImage> onPress) {
        this.sprite = sprite;
        this.onPress = onPress;
    }

    public SaturnClickableImage(Identifier sprite, Runnable onPress) {
        this(sprite, (m) -> {
            onPress.run();
        });
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, sprite, x, y, 0, 0, width, height, width,
                height, hovering || selected ? SaturnClient.COLOR : ColorHelper.getWhite(alpha));
    }

    @Override
    public void click() {
        this.onPress.accept(this);
    }

    public SaturnClickableImage setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
