package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.CrosshairInterface;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Textures;

public class Crosshair extends Module {

    public static Property<Boolean> enabled = Property.bool(false);

    private final CrosshairInterface minecraft;

    public Crosshair(CrosshairInterface minecraft) {
        super(
                new ModuleDetails("Crosshair", "crosshair")
                        .description("Changes the crosshair if you're aiming at a entity")
                        .tags("Visuals", "Utility")
                        .version("v0.1.0"),
                enabled.named("Enabled"));

        this.minecraft = minecraft;
    }

    @Override
    public void render(RenderScope scope) {

        if (enabled.value && minecraft.isTargetingLivingEntity()) {

            int scaledWidth = 15;
            int scaledHeight = 15;

            scope.drawTexture(
                    Textures.CROSSHAIR_RANGE,
                    (scope.getScaledWindowWidth() - scaledWidth) / 2,
                    (scope.getScaledWindowHeight() - scaledHeight) / 2,
                    0.0F,
                    0.0F,
                    scaledWidth,
                    scaledHeight);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}