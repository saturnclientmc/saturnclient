package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.math.Vec3d;

public class HealthDisplay extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> displayMode = Property.select(0, "Health", "Hearts");
    private static Property<Integer> decimals = Property.select(0, "0", "1", "2");
    private static ModDimensions dimensions = new ModDimensions(40, 18);
    double speed = 0.;
    Vec3d velocity;
    double y = 0.;

    public HealthDisplay() {
        super(new ModuleDetails("Health display", "health")
            .description("Displays your current health")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            displayMode.named("Display mode"),
            decimals.named("Decimals"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderHealth(10, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        float health = SaturnClient.client.player.getHealth();
        switch (displayMode.value) {
            case 1:
                health = health / 2;
        }
        renderHealth(health, scope);
    }

    public void renderHealth(float health, RenderScope scope) {
        String text = "";

        switch (decimals.value) {
            case 0: 
                text = String.format("%.0f ", health);
                break;
            case 1: 
                text = String.format("%.1f ", health);
                break;
            case 2: 
                text = String.format("%.2f ", health);
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
