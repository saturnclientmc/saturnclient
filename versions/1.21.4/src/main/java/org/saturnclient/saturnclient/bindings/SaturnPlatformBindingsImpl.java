package org.saturnclient.saturnclient.bindings;

import java.util.UUID;

import org.saturnclient.common.bindings.SaturnPlatformBindings;
import org.saturnclient.saturnclient.SaturnClient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class SaturnPlatformBindingsImpl implements SaturnPlatformBindings {

    @Override
    public String getAccessToken() {
        var session = MinecraftClient.getInstance().getSession();
        if (session == null) {
            return null;
        }
        return session.getAccessToken();
    }

    @Override
    public UUID getUuid() {
        var session = MinecraftClient.getInstance().getSession();
        if (session == null) {
            return null;
        }
        return session.getUuidOrNull();
    }

    @Override
    public String getUsername() {
        var session = MinecraftClient.getInstance().getSession();
        if (session == null) {
            return null;
        }
        return session.getUsername();
    }

    @Override
    public void onClientStopping(Runnable handler) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> handler.run());
    }

    @Override
    public void logInfo(String message) {
        SaturnClient.LOGGER.info(message);
    }

    @Override
    public void logError(String message) {
        SaturnClient.LOGGER.error(message);
    }

    @Override
    public void logError(String message, Throwable throwable) {
        SaturnClient.LOGGER.error(message, throwable);
    }
}
