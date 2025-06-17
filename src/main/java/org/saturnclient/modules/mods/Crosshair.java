package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Textures;

import net.minecraft.client.render.RenderLayer;

public class Crosshair extends Module {
    public static Property<Boolean> enabled = Property.bool(false);

    public Crosshair() {
        super(
            new ModuleDetails("Crosshair", "crosshair")
            .description("Changes the crosshair if you're aiming at a entity")
            .tags("Visuals", "Utility")
            .version("v0.1.0"),
            enabled.named("Enabled"));
    }

    @Override
    public void render(RenderScope scope) {
        if (enabled.value
                && SaturnClient.client.targetedEntity != null
                && SaturnClient.client.targetedEntity.isAlive()) {
            int scaledWidth = 15;
            int scaledHeight = 15;

            scope.setRenderLayer(RenderLayer::getCrosshair);
            scope.drawTexture(Textures.CROSSHAIR_RANGE,
                    (scope.getScaledWindowWidth() - scaledWidth) / 2,
                    (scope.getScaledWindowHeight() - scaledHeight) / 2, 0.0F, 0.0F, scaledWidth, scaledHeight);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}
