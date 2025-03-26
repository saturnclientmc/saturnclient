package org.saturnclient.saturnmods;

import net.minecraft.client.gui.DrawContext;

public interface HudMod {
    public ModDimensions getDimensions();

    public void renderDummy(DrawContext context);

    public void render(DrawContext context);
}
