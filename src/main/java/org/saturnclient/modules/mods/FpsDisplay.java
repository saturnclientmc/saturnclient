package org.saturnclient.modules.mods;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.gui.DrawContext;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class FpsDisplay extends Module implements HudMod {

    public static ConfigManager config = new ConfigManager("Fps Display");

    public static ModDimensions dimensions = new ModDimensions(
            config,
            0,
            0,
            40,
            8);

    public static Property<Integer> fgColor = config.property(
            "Foreground color",
            new Property<>(SaturnClientConfig.WHITE, Property.PropertyType.HEX));


    public FpsDisplay() {
        super(config, "Fps Display", "fps");
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public void renderDummy(DrawContext context) {
        int fps = 128;

        context.drawText(
                SaturnClientConfig.textRenderer,
                String.valueOf(fps) + " FPS",
                0,
                0,
                fgColor.value,
                false);
    }

    @Override
    public void render(DrawContext context) {
        int fps = SaturnClient.client.getCurrentFps();

        context.drawText(
                SaturnClientConfig.textRenderer,
                String.valueOf(fps) + " FPS",
                0,
                0,
                fgColor.value,
                false);
    }
}
