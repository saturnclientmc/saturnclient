package org.saturnclient.ui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnButton extends SaturnWidget {

    public boolean active = true;
    public String text;
    public Consumer<SaturnButton> onPress;
    public boolean background = true;

    private float hoverAlpha;

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

        int hoverColor = ((int) (alpha * 255) << 24) |
                (SaturnClient.COLOR.value & 0x00FFFFFF);

        if (background) {
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
                if (hoverAlpha == 0.0f)
                    hoverAlpha = 0.1f;

                SaturnUi.drawHighResGuiTexture(
                        context,
                        Textures.BUTTON_BORDER,
                        0,
                        0,
                        this.width,
                        this.height,
                        ((int) (hoverAlpha * 255) << 24) | (hoverColor));
            } else {
                hoverAlpha = 0.0f;
            }
        }

        if (alpha != 0.0f) {
            int i = this.active ? 16777215 : 10526880;
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
                    hovering
                            ? hoverColor
                            : i | (MathHelper.ceil(this.alpha * 255.0F) << 24),
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
            hoverAlpha += 0.06f;
        else if (hoverAlpha > 0.9f && hoverAlpha < 1.0f)
            hoverAlpha = 1.0f;
    }
}
