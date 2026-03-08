package org.saturnclient.impl.provider;

import java.awt.image.BufferedImage;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.common.provider.SaturnProvider;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.game.MinecraftClientRef;
import org.saturnclient.cosmetics.cloak.utils.IdentifierUtils;
import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.util.Identifier;

public class SaturnProviderImpl implements SaturnProvider {
    @Override
    public MinecraftClientRef getClient() {
        return (MinecraftClientRef) SaturnClient.client;
    }

    @Override
    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(SaturnClient.client.getWindow().getHandle(), key) == GLFW.GLFW_PRESS
                && SaturnClient.client.currentScreen == null;
    }

    @Override
    public void registerBufferedImageTexture(IdentifierRef i, BufferedImage bi) {
        IdentifierUtils.registerBufferedImageTextureFast((Identifier) (Object) i, bi);
    }

    @Override
    public String getKeyName(int key) {
        return GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
    }

    @Override
    public void stop() {
        SaturnClient.client.scheduleStop();
    }
}
