package org.saturnclient.impl;

import java.awt.image.BufferedImage;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.common.minecraft.IMinecraftClient;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.cosmetics.cloak.utils.IdentifierUtils;
import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class SaturnClientProvider extends MinecraftProvider {
    @Override
    public IMinecraftClient getClient() {
        return (IMinecraftClient) MinecraftClient.getInstance();
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
}
