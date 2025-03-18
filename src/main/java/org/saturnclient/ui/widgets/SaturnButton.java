package org.saturnclient.ui.widgets;

import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class SaturnButton extends SaturnWidget {
    public boolean active = true;
    public String text;
    public Consumer<SaturnButton> onPress;
    public boolean background = true;

    public SaturnButton(String text, Consumer<SaturnButton> onPress) {
        this.text = text;
        this.onPress = onPress;
    }

    public SaturnButton(String text, Runnable onPress) {
        this(text, (b) -> {
            onPress.run();
        });
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        int hoverColor = ((int) (alpha * 255) << 24) | (SaturnClient.COLOR & 0x00FFFFFF);

        if (background) {
            RenderSystem.setShaderTexture(0, Textures.BUTTON);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            context.drawGuiTexture(RenderLayer::getGuiTextured, Textures.BUTTON, this.x,
                    this.y, this.width, this.height, ColorHelper.getWhite(this.alpha));

            if (hovering) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, Textures.BUTTON_BORDER, this.x,
                        this.y, this.width, this.height, hoverColor);
            }
        }

        if (alpha != 0.0f) {
            int i = this.active ? 16777215 : 10526880;
            context.drawText(minecraftClient.textRenderer,
                    SaturnUi.text(text),
                    this.x + ((this.width - minecraftClient.textRenderer.getWidth(SaturnUi.text(text))) / 2),
                    this.y + ((this.height - minecraftClient.textRenderer.fontHeight + 1) / 2),
                    hovering ? hoverColor : i | MathHelper.ceil(this.alpha * 255.0F) << 24, false);
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        this.onPress.accept(this);
    }
}
