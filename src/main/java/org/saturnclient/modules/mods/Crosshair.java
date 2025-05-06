package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Textures;

import net.minecraft.client.render.RenderLayer;

public class Crosshair extends Module {
    public static Property<Boolean> enabled = new Property<>(false);
    public static Property<Boolean> range_indicator = new Property<>(false);

    public Crosshair() {
        super("Crosshair", "crosshair", "Changes the crosshair if you're aiming at a entity", enabled.named("Enabled"), range_indicator.named("Range Indicator"));
    }

    @Override
    public void render(RenderScope scope) {
        if (enabled.value
                && range_indicator.value
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
