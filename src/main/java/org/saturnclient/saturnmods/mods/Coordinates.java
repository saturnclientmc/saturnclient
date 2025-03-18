package org.saturnclient.saturnmods.mods;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Coordinates implements SaturnMod, HudMod {
    private static ModDimensions dimensions = new ModDimensions();
    public static boolean enabled = false;

    public Coordinates() {
    }

    public void renderDummy(DrawContext context) {
        int playerX = 124;
        int playerY = 69;
        int playerZ = 83;

        context.drawText(SaturnClient.textRenderer, playerX + " " + playerY + " " + playerZ,
                0, 0, SaturnClient.WHITE, false);

        dimensions.width = SaturnClient.textRenderer.getWidth(playerX + " " + playerY + " " + playerZ);
        dimensions.height = SaturnClient.textRenderer.fontHeight;
    }

    public void render(DrawContext context) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();
        int playerZ = (int) player.getZ();

        context.drawText(SaturnClient.textRenderer, playerX + " " + playerY + " " + playerZ,
                0, 0, SaturnClient.WHITE, false);
    }

    public String getName() {
        return "Coords";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("coords");
    }

    public void setDimensions(ModDimensions d) {
        dimensions = d;
    }

    public ModDimensions getDimensions() {
        return dimensions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
    }
}
