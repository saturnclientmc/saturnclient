package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.CoordinatesInterface;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class Coordinates extends Module implements HudMod {

    private static Property<Boolean> enabled = Property.bool(false);

    private static Property<Integer> displayMethod = Property.select(
            0,
            "Flat",
            "Flat annotated",
            "Newline",
            "Newline annotated");

    private static ModDimensions dimensions = new ModDimensions(40, 18);

    private final CoordinatesInterface minecraft;

    public Coordinates(CoordinatesInterface minecraft) {
        super(new ModuleDetails("Coordinates", "coords")
                .description("Displays your current coordinates")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                displayMethod.named("Display Method"),
                dimensions.prop());

        this.minecraft = minecraft;
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderCoords(532, 69, 253, scope);
    }

    @Override
    public void renderHud(RenderScope scope) {
        int x = minecraft.getPlayerX();
        int y = minecraft.getPlayerY();
        int z = minecraft.getPlayerZ();

        renderCoords(x, y, z, scope);
    }

    public void renderCoords(int x, int y, int z, RenderScope scope) {

        String f = "%d %d %d";

        switch (displayMethod.value) {
            case 1:
                f = "X: %d Y: %d Z: %d";
                break;

            case 2:
                f = "%d\n%d\n%d";
                break;

            case 3:
                f = "X: %d\nY: %d\nZ: %d";
                break;
        }

        String text = String.format(f, x, y, z);

        scope.drawText(
                text,
                0,
                0,
                dimensions.font.value,
                dimensions.fgColor.value);

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