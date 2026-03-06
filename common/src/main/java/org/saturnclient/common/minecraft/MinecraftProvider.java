package org.saturnclient.common.minecraft;

import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import java.awt.image.BufferedImage;

public abstract class MinecraftProvider {
    public static MinecraftProvider PROVIDER;

    public abstract IMinecraftClient getClient();

    public abstract Object createIdentifier(String namespace, String path);

    public abstract boolean isKeyPressed(int key);

    public abstract int getWidth(String text, int font);

    public abstract void registerBufferedImageTexture(SaturnIdentifier i, BufferedImage bi);

    public abstract String getKeyName(int key);
}
