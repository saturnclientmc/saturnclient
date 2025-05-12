package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.dialog.ColorDialog;
import org.saturnclient.ui2.resources.Fonts;

public class HexInput extends Element {
    public Property<Integer> prop;
    public SaturnScreen screen;

    public HexInput(Property<Integer> prop, SaturnScreen screen) {
        this.width = 50;
        this.prop = prop;
        this.height = Fonts.getHeight() + 8;
        this.scale = 0.8f;
        this.screen = screen;
    }

    @Override
    public void click(int mouseX, int mouseY) {
        SaturnClient.client.setScreen(new ColorDialog(prop, screen));
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, prop.value);
    }
}
