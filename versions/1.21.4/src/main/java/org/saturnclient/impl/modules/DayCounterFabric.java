package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.DayCounterInterface;

import net.minecraft.client.MinecraftClient;

public class DayCounterFabric implements DayCounterInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public long getDay() {
        if (mc.world == null) {
            return 0;
        }

        return mc.world.getTimeOfDay() / 24000L;
    }
}