package org.saturnclient.common.minecraft;

public abstract class MinecraftProvider {
    public static MinecraftProvider PROVIDER;

    public abstract Object createIdentifier(String namespace, String path);

    public abstract boolean isKeyPressed(int key);
}
