package org.saturnclient.ui.components;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.ColorHelper;

public class SaturnModComp extends SaturnWidget {
    SaturnMod mod;

    public SaturnModComp(SaturnMod mod) {
        this.mod = mod;
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        int color = mod.isEnabled() || hovering ? SaturnClient.COLOR : ColorHelper.getWhite(alpha);

        context.drawText(SaturnClient.textRenderer, mod.getName(), x + 27,
                y + (21 - SaturnClient.textRenderer.fontHeight) / 2,
                color, false);

        context.drawTexture(RenderLayer::getGuiTextured, Textures.MOD, x, y, 0, 0, 86, 20, width,
                height, color);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        mod.setEnabled(!mod.isEnabled());
    }
}
