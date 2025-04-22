package org.saturnclient.saturnmods.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

public class Coordinates implements SaturnMod, HudMod {

    private static ConfigManager config = new ConfigManager("Coordinates");
    public static Property<Boolean> enabled = config.property(
            "Enabled",
            new Property<>(false));
    private static ModDimensions dimensions = new ModDimensions(config);

    public static Property<Integer> fgColor = config.property(
            "Foreground color",
            new Property<>(SaturnClientConfig.WHITE, Property.PropertyType.HEX));

    public Coordinates() {
        // Ensure fgColor has a value
        if (fgColor.value == null) {
            fgColor.value = SaturnClientConfig.WHITE;
        }
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

    public String getName() {
        return "Coords";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("coords");
    }

    public ModDimensions getDimensions() {
        return dimensions;
    }

    public boolean isEnabled() {
        return enabled.value;
    }

    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }
}
