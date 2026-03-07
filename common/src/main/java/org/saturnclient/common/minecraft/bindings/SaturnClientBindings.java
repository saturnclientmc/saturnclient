package org.saturnclient.common.minecraft.bindings;

public final class SaturnClientBindings {
    private static SaturnPlatformBindings platform;
    private static SaturnEmoteBindings emotes;

    private SaturnClientBindings() {
    }

    public static void setPlatform(SaturnPlatformBindings bindings) {
        platform = bindings;
    }

    public static SaturnPlatformBindings platform() {
        if (platform == null) {
            throw new IllegalStateException("SaturnPlatformBindings not initialized");
        }
        return platform;
    }

    public static void setEmotes(SaturnEmoteBindings bindings) {
        emotes = bindings;
    }

    public static SaturnEmoteBindings emotes() {
        if (emotes == null) {
            throw new IllegalStateException("SaturnEmoteBindings not initialized");
        }
        return emotes;
    }
}
