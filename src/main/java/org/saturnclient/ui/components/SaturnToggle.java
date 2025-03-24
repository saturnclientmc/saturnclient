package org.saturnclient.ui.components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.ColorHelper;
import org.saturnclient.saturnclient.SaturnClient;
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
        int mouseY
    ) {
        int color = prop.value
            ? SaturnClient.COLOR
            : SaturnClient.getWhite(alpha);
        int w = 13;
        int h = 6;
        int s = 8;

        context.drawTexture(
            RenderLayer::getGuiTextured,
            Textures.TOGGLE_BG,
            x,
            y + 1,
            0,
            0,
            w,
            h,
            w,
            h,
            color
        );

        context.drawTexture(
            RenderLayer::getGuiTextured,
            Textures.TOGGLE_INDICATOR,
            prop.value ? x + 5 : x,
            y,
            0,
            0,
            s,
            s,
            s,
            s,
            color
        );
    }

    @Override
    public void click(int mouseX, int mouseY) {
        prop.value = !prop.value;
    }
}
