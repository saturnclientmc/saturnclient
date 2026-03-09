package org.saturnclient.impl.modules;

import net.minecraft.client.MinecraftClient;

import org.saturnclient.common.module.WorldModule;

/**
 * Fabric implementation of {@link WorldModule}.
 *
 * Reads the client world's time directly from {@link MinecraftClient}.
 * Returns safe defaults when no world is loaded.
 */
public class WorldModuleFabric implements WorldModule {

    private final MinecraftClient mc;

    public WorldModuleFabric(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public boolean hasWorld() {
        return mc.world != null;
    }

    /**
     * Returns the total world age in ticks, or {@code 0} when no world
     * is loaded.  Used by {@link TpsFeature#onTimePacket} (which is
     * called from the mixin and stores its own copy) and by
     * {@link DayCounterFeature} for the day count.
     */
    @Override
    public long getWorldAge() {
        return hasWorld() ? mc.world.getTime() : 0L;
    }
}
