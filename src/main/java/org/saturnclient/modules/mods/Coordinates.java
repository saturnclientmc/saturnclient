package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.manager.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.math.Vec3d;

public class Coordinates extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> displayMethod = Property.select(0, "Flat", "Flat annotated", "Newline", "Newline annotated");
    private static ModDimensions dimensions = new ModDimensions(40, 18);

    public Coordinates() {
        super(new ModuleDetails("Coordinates", "coords")
            .description("Displays your current coordinates")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            displayMethod.named("Display Method"),
            dimensions.prop()
        );
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderCoords(532, 69, 253, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        Vec3d pos = SaturnClient.client.player.getPos();
        renderCoords((int) pos.x, (int) pos.y, (int) pos.z, scope);
    }

    public void renderCoords(int x, int y, int z, RenderScope scope) {
        String f = "%d %d %d"; // Flat
        switch (displayMethod.value) {
            case 1: // Flat annotated
                f = "X: %d Y: %d Z: %d";
                break;
            case 2: // Newline
                f = "%d\n%d\n%d";
                break;
            case 3: // Newline annotated
                f = "X: %d\nY: %d\nZ: %d";
                break;
        }
        String text = String.format(f, x, y, z);
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
