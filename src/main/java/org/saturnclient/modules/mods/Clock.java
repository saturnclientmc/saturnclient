package org.saturnclient.modules.mods;

import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class Clock extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> format = Property.select(0, "24 hour", "12 hour");
    private static Property<Boolean> seconds = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());

    public Clock() {
        super(new ModuleDetails("Clock", "clock")
            .description("Displays the current real-world time")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            format.named("Format"),
            seconds.named("Show seconds"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderClock(scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderClock(scope);
    }

    public void renderClock(RenderScope scope) {
        String text = "";
        LocalTime currentTime = LocalTime.now();

        switch (format.value) {
            case 0:
                if (seconds.value) { text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); }
                else { text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm")); }
                break;
            case 1: 
                if (seconds.value) { text = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a")); }
                else { text = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a")); }
                break;
        }

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