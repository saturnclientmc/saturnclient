package org.saturnclient.ui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.ConfigEditor;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnModComp extends SaturnWidget {

    SaturnMod mod;

    public SaturnModComp(SaturnMod mod) {
        this.mod = mod;
    }

    @Override
    public void render(
        DrawContext context,
        boolean hovering,
        int mouseX,
        int mouseY
    ) {
        boolean enabled = mod.isEnabled();

        boolean hovering_settings = settingsHovering(mouseX, mouseY);

        int color = !hovering_settings && (enabled || hovering)
            ? SaturnClient.COLOR.value
            : SaturnClient.getWhite(alpha);

        int size = 12;

        if (enabled) {
            context.drawTexture(
                RenderLayer::getGuiTextured,
                Textures.MOD_BG,
                0,
                0,
                0,
                0,
                86,
                20,
                86,
                20,
                SaturnClient.COLOR.value
            );
        }

        context.drawTexture(
            RenderLayer::getGuiTextured,
            Textures.MOD,
            0,
            0,
            0,
            0,
            86,
            20,
            86,
            20,
            color
        );

        context.drawTexture(
            RenderLayer::getGuiTextured,
            mod.getIconTexture(),
            4,
            4,
            0,
            0,
            size,
            size,
            size,
            size,
            color
        );

        context.drawText(
            SaturnClient.textRenderer,
            SaturnUi.text(mod.getName()),
            20,
            (21 - SaturnClient.textRenderer.fontHeight) / 2,
            color,
            false
        );

        context.drawTexture(
            RenderLayer::getGuiTextured,
            Textures.SETTINGS,
            77,
            11,
            0,
            0,
            7,
            7,
            7,
            7,
            hovering_settings
                ? SaturnClient.COLOR.value
                : SaturnClient.getWhite(alpha)
        );
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (settingsHovering(mouseX, mouseY)) {
            MinecraftClient.getInstance()
                .setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    public boolean settingsHovering(int mouseX, int mouseY) {
        int sX = 77;
        int sY = 11;
        int size = 7;

        return (
            sX < mouseX &&
            sX + size > mouseX &&
            sY < mouseY &&
            sY + size > mouseY
        );
    }
}
