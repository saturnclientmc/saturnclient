package org.saturnclient.saturnclient.widgets;

import java.awt.Color;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SaturnButton extends TexturedButtonWidget {
    private TextWidget textWidget;
    private static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("widget/saturn_button"), Identifier.ofVanilla("widget/saturn_button_disabled"),
            Identifier.ofVanilla("widget/saturn_button_highlighted"));

    public SaturnButton(Text message, PressAction onPress, int x, int y, int width, int height) {
        super(x, y, width, height, BUTTON_TEXTURES, onPress);

        setMessage(message);

        textWidget = new TextWidget(message,
                MinecraftClient.getInstance().textRenderer);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        context.drawText(MinecraftClient.getInstance().textRenderer, getMessage(),
                getX() + (getWidth() - textWidget.getWidth()) / 2,
                1 + getY() + (getHeight() - textWidget.getHeight()) / 2,
                Color.WHITE.getRGB(), false);
    }
}
