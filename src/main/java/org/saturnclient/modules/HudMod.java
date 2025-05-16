package org.saturnclient.modules;

import org.saturnclient.ui2.RenderScope;

public interface HudMod {
    public ModDimensions getDimensions();

    public void renderDummy(RenderScope scope);

    public void renderHud(RenderScope scope);
}
