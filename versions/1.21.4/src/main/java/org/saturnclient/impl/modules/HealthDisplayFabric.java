package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.HealthDisplayInterface;

import net.minecraft.client.MinecraftClient;

public class HealthDisplayFabric implements HealthDisplayInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public float getPlayerHealth() {
        if (mc.player == null) {
            return 0f;
        }

        return mc.player.getHealth();
    }

}