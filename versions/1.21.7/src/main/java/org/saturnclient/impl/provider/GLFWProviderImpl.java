package org.saturnclient.impl.provider;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.common.provider.GLFWProvider;
import org.saturnclient.saturnclient.SaturnClient;

public class GLFWProviderImpl implements GLFWProvider {
    @Override
    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(SaturnClient.client.getWindow().getHandle(), key) == GLFW.GLFW_PRESS
                && SaturnClient.client.currentScreen == null;
    }

    @Override
    public String getKeyName(int key) {
        return GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
    }
}
