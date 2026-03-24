package org.saturnclient.impl.features.network;

import org.saturnclient.common.feature.NetworkFeature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

/**
 * Fabric implementation of {@link NetworkFeature}.
 *
 * Connection presence is checked via the network handler.
 * Ping is read from the player's {@link PlayerListEntry} which the
 * server populates; returns {@code -1} when unavailable.
 */
public class NetworkFeatureImpl implements NetworkFeature {

    private final MinecraftClient mc;

    public NetworkFeatureImpl(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public boolean hasNetwork() {
        return mc.getNetworkHandler() != null;
    }

    @Override
    public int getPing() {
        if (mc.player == null || mc.getNetworkHandler() == null)
            return -1;

        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());

        return entry != null ? entry.getLatency() : -1;
    }
}
