package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

import com.mojang.blaze3d.systems.RenderSystem;

public class SaturnImageButton extends SaturnWidget {

    public Identifier sprite;
    public int imageWidth;
    public int imageHeight;
    public Consumer<SaturnImageButton> onPress;

    public SaturnImageButton(
            Identifier sprite,
            int imageWidth,
            int imageHeight,
            Consumer<SaturnImageButton> onPress) {
        this.sprite = sprite;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.onPress = onPress;
    }

    public SaturnImageButton(
            Identifier sprite,
            int imageWidth,
            int imageHeight,
            Runnable onPress) {
        this(sprite, imageWidth, imageHeight, _o -> {
            onPress.run();
        });
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        int hoverColor = ((int) (alpha * 255) << 24) |
                (SaturnClient.COLOR.value & 0x00FFFFFF);
        RenderSystem.setShaderTexture(0, Textures.BUTTON);

        SaturnUi.drawHighResGuiTexture(
                context,
                Textures.BUTTON,
                0,
                0,
                this.width,
                this.height,
                SaturnClient.getWhite(this.alpha));

        if (hovering) {
            SaturnUi.drawHighResGuiTexture(
                    context,
                    Textures.BUTTON_BORDER,
                    0,
                    0,
                    this.width,
                    this.height,
                    hoverColor);
        }

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
                hovering ? hoverColor : SaturnClient.getWhite(alpha));
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }
}
