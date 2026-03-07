package org.saturnclient.common.minecraft.bindings;

import java.util.UUID;

public interface SaturnPlatformBindings {
    String getAccessToken();

    UUID getUuid();

    String getUsername();

    void onClientStopping(Runnable handler);

    void logInfo(String message);

    void logError(String message);

    void logError(String message, Throwable throwable);
}
