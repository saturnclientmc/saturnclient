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

public class Coordinates extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(40, 18);

    public Coordinates() {
        super(new ModuleDetails("Coordinates", "coords")
            .description("Displays your current coordinates")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        String text = String.format("X: %.0f Y: %.0f Z: %.0f", 532, 69, 253);
        scope.drawText(text,
            0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public void renderHud(RenderScope scope) {
        Vec3d pos = SaturnClient.client.player.getPos();
        String text = String.format("X: %.0f Y: %.0f Z: %.0f", pos.x, pos.y, pos.z);
        scope.drawText(text,
            0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
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
    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}
