package org.saturnclient.modules.mods;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class Coordinates extends Module implements HudMod {

    private static ConfigManager config = new ConfigManager("Coordinates");
    private static ModDimensions dimensions = new ModDimensions(config);

    public static Property<Integer> fgColor = config.property(
            "Foreground color",
            new Property<>(SaturnClientConfig.WHITE, Property.PropertyType.HEX));

    public Coordinates() {
        super(config, "Coordinates", "coords");
    }

    public void renderDummy(DrawContext context) {
        int playerX = 124;
        int playerY = 69;
        int playerZ = 83;

        context.drawText(
                SaturnClientConfig.textRenderer,
                playerX + " " + playerY + " " + playerZ,
                0,
                0,
                fgColor.value,
                false);

        dimensions.width = SaturnClientConfig.textRenderer.getWidth(
                playerX + " " + playerY + " " + playerZ);
        dimensions.height = SaturnClientConfig.textRenderer.fontHeight;
    }

    public void render(DrawContext context) {
        PlayerEntity player = SaturnClient.client.player;
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();
        int playerZ = (int) player.getZ();

        context.drawText(
                SaturnClientConfig.textRenderer,
                playerX + " " + playerY + " " + playerZ,
                0,
                0,
                fgColor.value,
                false);
    }

    public ModDimensions getDimensions() {
        return dimensions;
    }
}
