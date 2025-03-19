package org.saturnclient.ui.components;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.SaturnUi;
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
        boolean enabled = mod.isEnabled();

        boolean hovering_settings = settingsHovering(mouseX, mouseY);

        int color = !hovering_settings && (enabled || hovering) ? SaturnClient.COLOR : ColorHelper.getWhite(alpha);

        int size = 12;

        if (enabled) {
            context.drawTexture(RenderLayer::getGuiTextured, Textures.MOD_BG, x, y, 0, 0, 86, 20, 86,
                    20, SaturnClient.COLOR);
        }

        context.drawTexture(RenderLayer::getGuiTextured, Textures.MOD, x, y, 0, 0, 86, 20, 86,
                20, color);

        context.drawTexture(RenderLayer::getGuiTextured, mod.getIconTexture(), x + 4, y + 4, 0, 0, size, size,
                size,
                size, color);

        context.drawText(SaturnClient.textRenderer, SaturnUi.text(mod.getName()), x + 20,
                y + (21 - SaturnClient.textRenderer.fontHeight) / 2,
                color, false);

        context.drawTexture(RenderLayer::getGuiTextured, Textures.SETTINGS, x + 77,
                y + 11, 0, 0, 7, 7, 7, 7, hovering_settings ? SaturnClient.COLOR : ColorHelper.getWhite(alpha));
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (settingsHovering(mouseX, mouseY)) {
            // TODO: make a config thingy, and implement it on the mods
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    public boolean settingsHovering(int mouseX, int mouseY) {
        int sX = x + 77;
        int sY = y + 11;
        int size = 7;

        return sX < mouseX && sX + size > mouseX
                && sY < mouseY && sY + size > mouseY;
    }
}
