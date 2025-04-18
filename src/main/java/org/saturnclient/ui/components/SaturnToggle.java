package org.saturnclient.ui.components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnToggle extends SaturnWidget {

    Property<Boolean> prop;

    public SaturnToggle(Property<Boolean> prop) {
        this.prop = prop;
        width = 16;
        height = 8;
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        int color = prop.value
                ? SaturnClientConfig.color.value
                : SaturnClientConfig.getWhite(alpha);
        int w = 13;
        int h = 6;
        int s = 8;

        context.drawTexture(
                RenderLayer::getGuiTextured,
                Textures.TOGGLE_BG,
                0,
                1,
                0,
                0,
                w,
                h,
                w,
                h,
                color);

        context.drawTexture(
                RenderLayer::getGuiTextured,
                Textures.TOGGLE_INDICATOR,
                prop.value ? 5 : 0,
                0,
                0,
                0,
                s,
                s,
                s,
                s,
                color);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        prop.value = !prop.value;
        ConfigManager.save();
    }
}
