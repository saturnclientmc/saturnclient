package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class Fps extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());
    
    public Fps() {
        super(new ModuleDetails("FPS Display", "fps")
            .description("Displays current FPS")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            dimensions.prop());
    }

    @Override
    public void renderHud(RenderScope scope) {
        String text = String.valueOf(SaturnClient.client.getCurrentFps()) + " FPS";
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public void renderDummy(RenderScope scope) {
        String text = "369 FPS";
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }
}
