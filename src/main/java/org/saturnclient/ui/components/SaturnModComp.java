package org.saturnclient.ui.components;

import net.minecraft.client.gui.DrawContext;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.menus.ConfigEditor;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnModComp extends SaturnWidget {

    Module mod;

    public SaturnModComp(Module mod) {
        this.mod = mod;
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        boolean enabled = mod.isEnabled();

        boolean hovering_settings = settingsHovering(mouseX, mouseY);

        int color = !hovering_settings && (enabled || hovering)
                ? SaturnClientConfig.color.value
                : SaturnClientConfig.getWhite(alpha);

        int size = 12;

        if (enabled) {
            SaturnUi.drawHighResTexture(
                    context,
                    Textures.MOD_BG,
                    0,
                    0,
                    86,
                    20,
                    SaturnClientConfig.color.value);
        }

        SaturnUi.drawHighResTexture(
                context,
                Textures.MOD,
                0,
                0,
                86,
                20,
                color);

        SaturnUi.drawHighResTexture(
                context,
                mod.getIconTexture(),
                4,
                4,
                size,
                size,
                color);

        context.drawText(
                SaturnClient.client.textRenderer,
                SaturnUi.text(mod.getName()),
                20,
                (21 - SaturnClient.client.textRenderer.fontHeight) / 2,
                color,
                false);

        SaturnUi.drawHighResTexture(
                context,
                Textures.SETTINGS,
                77,
                11,
                7,
                7,
                hovering_settings
                        ? SaturnClientConfig.color.value
                        : SaturnClientConfig.getWhite(alpha));
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (settingsHovering(mouseX, mouseY)) {
            SaturnClient.client
                    .setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    public boolean settingsHovering(int mouseX, int mouseY) {
        int sX = 77;
        int sY = 11;
        int size = 7;

        return (sX < mouseX &&
                sX + size > mouseX &&
                sY < mouseY &&
                sY + size > mouseY);
    }
}
