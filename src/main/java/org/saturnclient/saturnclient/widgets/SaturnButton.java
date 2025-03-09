package org.saturnclient.saturnclient.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class SaturnButton extends ButtonWidget {
    private static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("widget/saturn_button"), Identifier.ofVanilla("widget/saturn_button_disabled"),
            Identifier.ofVanilla("widget/saturn_button_highlighted"));

    protected SaturnButton(int x, int y, int width, int height, Text message, PressAction onPress,
            NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    public static SaturnButton builder(Text message, PressAction onPress, int x, int y, int width, int height) {
        return new SaturnButton(x, y, width, height, message, onPress, null);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURES.get(this.active, this.isSelected()), this.getX(),
                this.getY(), this.getWidth(), this.getHeight(), ColorHelper.getWhite(this.alpha));

        int i = this.active ? 16777215 : 10526880;

        this.drawMessage(context, minecraftClient.textRenderer, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
