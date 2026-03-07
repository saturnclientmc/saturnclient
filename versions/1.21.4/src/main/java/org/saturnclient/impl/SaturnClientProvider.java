package org.saturnclient.impl;

import java.awt.image.BufferedImage;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.common.minecraft.IMinecraftClient;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.cosmetics.cloak.utils.IdentifierUtils;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.SaturnScreenFabric;
import org.saturnclient.ui.screens.TitleMenu;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.util.Identifier;

public class SaturnClientProvider extends MinecraftProvider {
    @Override
    public IMinecraftClient getClient() {
        return (IMinecraftClient) SaturnClient.client;
    }

    @Override
    public Object createIdentifier(String namespace, String path) {
        return Identifier.of(namespace, path);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(SaturnClient.client.getWindow().getHandle(), key) == GLFW.GLFW_PRESS
                && SaturnClient.client.currentScreen == null;
    }

    @Override
    public int getWidth(String text, int font) {
        return Fonts.getWidth(text, font);
    }

    @Override
    public void registerBufferedImageTexture(SaturnIdentifier i, BufferedImage bi) {
        IdentifierUtils.registerBufferedImageTextureFast((Identifier) i.inner, bi);
    }

    @Override
    public String getKeyName(int key) {
        return GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
    }

    @Override
    public void setScreen(SaturnScreen screen) {
        SaturnClient.client.setScreen(new SaturnScreenFabric(screen));
    }

    @Override
    public void setScreen(MinecraftScreen screen) {
        SaturnScreenFabric exitTarget = new SaturnScreenFabric(new TitleMenu());

        switch (screen) {
            case SelectWorld:
                SaturnClient.client.setScreen(new SelectWorldScreen(exitTarget));
                break;

            case Multiplayer:
                SaturnClient.client.setScreen(new MultiplayerScreen(exitTarget));
                break;

            case Options:
                SaturnClient.client.setScreen(new OptionsScreen(exitTarget, SaturnClient.client.options));
                break;
        }
    }

    @Override
    public void stop() {
        SaturnClient.client.scheduleStop();
    }
}
