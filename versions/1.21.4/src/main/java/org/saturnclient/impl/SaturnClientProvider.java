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
}
