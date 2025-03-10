package org.saturnclient.saturnclient.widgets;

import java.util.function.Consumer;
import org.saturnclient.ui.SaturnWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class SaturnButton extends SaturnWidget {
    public boolean active = true;
    public String text;
    public Consumer<SaturnButton> onPress;

    public SaturnButton(String text, Consumer<SaturnButton> onPress) {
        this.text = text;
        this.onPress = onPress;
    }

    private static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("widget/saturn_button"), Identifier.ofVanilla("widget/saturn_button_disabled"),
            Identifier.ofVanilla("widget/saturn_button_highlighted"));

    @Override
    public void render(DrawContext context, boolean hovering) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURES.get(this.active, hovering), this.x,
                this.y, this.width, this.height, ColorHelper.getWhite(this.alpha));

        int i = this.active ? 16777215 : 10526880;
        context.drawText(minecraftClient.textRenderer, this.text,
                this.x + ((this.width - minecraftClient.textRenderer.getWidth(this.text)) / 2),
                this.y + ((this.height - 7) / 2),
                i | MathHelper.ceil(this.alpha * 255.0F) << 24, false);
    }

    @Override
    public void click() {
        this.onPress.accept(this);
    }
}
