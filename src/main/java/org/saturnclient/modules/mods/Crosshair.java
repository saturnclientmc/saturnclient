package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.saturnclient.config.Property;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class Crosshair extends Module {
    Identifier textureLocation = Identifier.of("saturnclient", "textures/gui/hud/crosshair_range.png");

    public static NamedProperty<Boolean> enabled = new Property<>(false).named("Enabled");
    public static NamedProperty<Boolean> range_indicator = new Property<>(false).named("Range Indicator");

    public Crosshair() {
        super("Crosshair", "crosshair", enabled, range_indicator);
    }

    @Override
    public void render(DrawContext context) {
        if (enabled.prop.value
                && range_indicator.prop.value
                && SaturnClient.client.targetedEntity != null
                && SaturnClient.client.targetedEntity.isAlive()) {
            RenderSystem.setShaderTexture(0, textureLocation);
            int scaledWidth = 15;
            int scaledHeight = 15;

            context.drawTexture(RenderLayer::getCrosshair, textureLocation,
                    (context.getScaledWindowWidth() - scaledWidth) / 2,
                    (context.getScaledWindowHeight() - scaledHeight) / 2, 0.0F, 0.0F, scaledWidth, scaledHeight,
                    scaledWidth, scaledHeight);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.prop.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.prop.value = e;
    }
}
