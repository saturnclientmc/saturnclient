package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.FpsInterface;

import net.minecraft.client.MinecraftClient;

public class FpsFabric implements FpsInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public int getFps() {
        return mc.getCurrentFps();
    }
}