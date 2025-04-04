package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;

public class SaturnClickableSprite extends SaturnWidget {

    public Identifier sprite;
    public boolean selected = false;
    public Consumer<SaturnClickableSprite> onPress;

    public SaturnClickableSprite(
            Identifier sprite,
            Consumer<SaturnClickableSprite> onPress) {
        this.sprite = sprite;
        this.onPress = onPress;
    }

    public SaturnClickableSprite(Identifier sprite, Runnable onPress) {
        this(sprite, _o -> {
            onPress.run();
        });
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        SaturnUi.drawHighResGuiTexture(
                context,
                sprite,
                0,
                0,
                this.width,
                this.height,
                SaturnClient.getColor(hovering, alpha));
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    public SaturnClickableSprite setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
