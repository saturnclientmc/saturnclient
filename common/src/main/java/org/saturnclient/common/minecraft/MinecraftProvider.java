package org.saturnclient.common.minecraft;

import java.io.File;

public abstract class MinecraftProvider {
    public static MinecraftProvider PROVIDER;

    public abstract Object createIdentifier(String namespace, String path);

    public abstract boolean isKeyPressed(int key);

    public abstract File getRunDirectory();
}
