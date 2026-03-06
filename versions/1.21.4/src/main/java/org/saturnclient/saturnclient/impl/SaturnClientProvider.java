package org.saturnclient.saturnclient.impl;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.util.Identifier;

public class SaturnClientProvider extends MinecraftProvider {
    @Override
    public Object createIdentifier(String namespace, String path) {
        return Identifier.of(namespace, path);
    }

    @Override
    public File getRunDirectory() {
        return SaturnClient.client.runDirectory;
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
}
