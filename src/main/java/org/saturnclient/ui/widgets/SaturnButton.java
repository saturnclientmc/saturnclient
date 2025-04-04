package org.saturnclient.ui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnButton extends SaturnWidget {

    public boolean active = true;
    public String text;
    public Consumer<SaturnButton> onPress;
    public boolean background = true;
    private float hoverAlpha = 0.0f;

    public SaturnButton(String text, Consumer<SaturnButton> onPress) {
        this.text = text;
        this.onPress = onPress;
    }

    public SaturnButton(String text, Runnable onPress) {
        this(text, b -> {
            onPress.run();
        });
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        if (background) {
            RenderSystem.setShaderTexture(0, Textures.BUTTON);

            SaturnUi.drawHighResGuiTexture(
                    context,
                    Textures.BUTTON,
                    0,
                    0,
                    this.width,
                    this.height,
                    SaturnClient.getColor(hovering, alpha));

            if (hovering) {
                if (hoverAlpha == 0.0f)
                    hoverAlpha = 0.1f;

                SaturnUi.drawHighResGuiTexture(
                        context,
                        Textures.BUTTON_BORDER,
                        0,
                        0,
                        this.width,
                        this.height,
                        SaturnClient.getColor(hovering, alpha));
            } else {
                hoverAlpha = 0.0f;
            }
        }

        if (alpha != 0.0f) {
            context.drawText(
                    minecraftClient.textRenderer,
                    SaturnUi.text(text),
                    ((this.width -
                            minecraftClient.textRenderer.getWidth(
                                    SaturnUi.text(text)))
                            /
                            2),
                    ((this.height - minecraftClient.textRenderer.fontHeight + 1) /
                            2),
                    SaturnClient.getColor(hovering, alpha),
                    false);
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }

    @Override
    public void tick() {
        if (hoverAlpha > 0.0f && hoverAlpha < 0.9f)
            hoverAlpha += 0.03f;
        else if (hoverAlpha > 0.9f && hoverAlpha < 1.0f)
            hoverAlpha = 1.0f;
    }
}
