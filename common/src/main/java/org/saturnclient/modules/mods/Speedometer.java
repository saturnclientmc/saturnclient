package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.config.manager.Property;
import org.saturnclient.modules.interfaces.SpeedometerInterface;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class Speedometer extends Module implements HudMod {

    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> axis = Property.select(1, "Absolute", "X axis", "Y axis");
    private static Property<Integer> unitText = Property.select(0, "None", "Blocks/s", "blocks/s", "b/s");
    private static Property<Boolean> speedText = Property.bool(true);
    private static ModDimensions dimensions = new ModDimensions(40, 18);

    private final SpeedometerInterface provider;
    private double speed = 0.;

    public Speedometer(SpeedometerInterface provider) {
        super(new ModuleDetails("Speedometer", "speed")
                .description("Displays your current speed")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                axis.named("Speed type"),
                unitText.named("Unit text"),
                speedText.named("Show speed text"),
                dimensions.prop());

        this.provider = provider;
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderSpeed(2.71, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        SpeedometerInterface.Velocity velocity = provider.getVelocity();
        boolean onGround = provider.isOnGround();

        double y = onGround ? 0.0 : velocity.y;

        switch (axis.value) {
            case 0: // Absolute
                speed = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(y, 2) + Math.pow(velocity.z, 2));
                break;
            case 1: // X axis
                speed = Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));
                break;
            case 2: // Y axis
                speed = Math.abs(y);
                break;
        }

        renderSpeed(speed * 20, scope); // blocks/tick to blocks/s
    }

    public void renderSpeed(double speed, RenderScope scope) {
        String text = String.format("%.2f ", speed);

        switch (unitText.value) {
            case 1 -> text += "Blocks/s";
            case 2 -> text += "blocks/s";
            case 3 -> text += "b/s";
        }

        if (speedText.value) {
            text = "Speed: " + text;
        }

        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
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