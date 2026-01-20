package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class DayCounter extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(40, 18);
    long day = 0;

    public DayCounter() {
        super(new ModuleDetails("DayCounter", "day")
            .description("Displays the number of days that have passed")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderDay(271, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        day = (SaturnClient.client.world.getTimeOfDay() / 24000L);
        renderDay(day, scope);
    }

    public void renderDay(long day, RenderScope scope) {
        String text = "Day: " + day;
        scope.drawText(text,
            0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
        dimensions.height = 18 * text.split("\n").length;
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}

