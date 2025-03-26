package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnWidget;

public class SaturnClickableSprite extends SaturnWidget {

    public Identifier sprite;
    public boolean selected = false;
    public Consumer<SaturnClickableSprite> onPress;
    public int color = SaturnClient.getWhite(1.0f);
    public int hoverColor = SaturnClient.COLOR;

    public SaturnClickableSprite(
        Identifier sprite,
        Consumer<SaturnClickableSprite> onPress
    ) {
        this.sprite = sprite;
        this.onPress = onPress;
    }

    public SaturnClickableSprite(Identifier sprite, Runnable onPress) {
        this(sprite, m -> {
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
        context.drawGuiTexture(
            RenderLayer::getGuiTextured,
            sprite,
            0,
            0,
            this.width,
            this.height,
            (hovering || selected ? hoverColor : color) |
            (MathHelper.ceil(this.alpha * 255.0F) << 24)
        );
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    public SaturnClickableSprite setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public SaturnClickableSprite setColor(int color) {
        this.color = color;
        return this;
    }

    public SaturnClickableSprite setHoverColor(int color) {
        this.hoverColor = color;
        return this;
    }
}
