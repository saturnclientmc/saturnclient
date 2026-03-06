package org.saturnclient.common.minecraft.bindings;

import org.saturnclient.common.minecraft.MinecraftBinding;
import org.saturnclient.common.minecraft.MinecraftProvider;

public class SaturnIdentifier extends MinecraftBinding {
    public SaturnIdentifier(String namespace, String path) {
        super(MinecraftProvider.PROVIDER.createIdentifier(namespace, path));
    }

    public static SaturnIdentifier of(String namespace, String path) {
        return new SaturnIdentifier(namespace, path);
    }

    public static SaturnIdentifier ofVanilla(String path) {
        return new SaturnIdentifier("minecraft", path);
    }
}
