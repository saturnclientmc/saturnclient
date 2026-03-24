package org.saturnclient.impl.features.world;

import org.saturnclient.common.feature.WorldFeature;

import net.minecraft.client.MinecraftClient;

/**
 * Fabric implementation of {@link WorldFeature}.
 *
 * Reads the client world's time directly from {@link MinecraftClient}.
 * Returns safe defaults when no world is loaded.
 */
public class WorldFeatureImpl implements WorldFeature {

    private final MinecraftClient mc;

    public WorldFeatureImpl(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public boolean hasWorld() {
        return mc.world != null;
    }

    /**
     * Returns the total world age in ticks, or {@code 0} when no world
     * is loaded. Used by {@link TpsFeature#onTimePacket} (which is
     * called from the mixin and stores its own copy) and by
     * {@link DayCounterFeature} for the day count.
     */
    @Override
    public long getWorldAge() {
        return hasWorld() ? mc.world.getTime() : 0L;
    }
}
