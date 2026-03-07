package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.FpsInterface;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class Fps extends Module implements HudMod {

    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());

    private final FpsInterface minecraft;

    public Fps(FpsInterface minecraft) {
        super(new ModuleDetails("FPS Display", "fps")
                .description("Displays current FPS")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                dimensions.prop());

        this.minecraft = minecraft;
    }

    @Override
    public void renderHud(RenderScope scope) {
        int fps = minecraft.getFps();

        String text = fps + " FPS";

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