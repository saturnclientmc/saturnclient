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
import net.minecraft.entity.Entity;

public class Speedometer extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> axis = Property.select(1, "Absolute", "X axis", "Y axis");
    private static Property<Integer> unitText = Property.select(0, "None", "Blocks/s", "blocks/s", "b/s");
    private static Property<Boolean> speedText = Property.bool(true);
    private static ModDimensions dimensions = new ModDimensions(40, 18);
    double speed = 0.;
    Vec3d velocity;

    public Speedometer() {
        super(new ModuleDetails("Speedometer", "coords")
            .description("Displays your current speed")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            axis.named("Speed type"),
            unitText.named("Unit text"),
            speedText.named("Show speed text"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderSpeed(2.71, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        Entity vehicle = SaturnClient.client.player.getVehicle();

        if (vehicle == null) {
            velocity = SaturnClient.client.player.getVelocity();
        } else {
            velocity = vehicle.getVelocity();
        }

        switch (axis.value) {
            case 0: // Absolute
                speed = (Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2) + Math.pow(velocity.z, 2)));
                break;

            case 1: // x axis
                speed = (Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2)));
                break;
            
            case 2: // y axis
                speed = (Math.abs(velocity.y));
                break;
        }
        renderSpeed(speed * 20, scope); // blocks/tick to blocks/s
    }

    public void renderSpeed(double speed, RenderScope scope) {
        String f = String.format("%.2f ", speed);

        switch (unitText.value) {
            case 1: 
                f += "Blocks/s";
                break;
            
            case 2: 
                f += "blocks/s";
                break;
            case 3: 
                f += "b/s";
                break;
        }

        String text = f;

        if (speedText.value) {
            text = "Speed: " + text;
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
