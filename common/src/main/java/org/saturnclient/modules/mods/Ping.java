package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.config.manager.Property;
import org.saturnclient.modules.interfaces.PingInterface;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class Ping extends Module implements HudMod {

    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());

    private final PingInterface pingProvider;

    public Ping(PingInterface pingProvider) {
        super(new ModuleDetails("Ping Display", "ping")
                .description("Displays ping")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                dimensions.prop());

        this.pingProvider = pingProvider;
    }

    @Override
    public void renderHud(RenderScope scope) {
        int ping = pingProvider.getPing();
        String text = ping + " ms";
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public void renderDummy(RenderScope scope) {
        String text = "10 ms";
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