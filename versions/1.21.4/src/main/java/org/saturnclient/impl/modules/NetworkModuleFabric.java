package org.saturnclient.impl.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import org.saturnclient.common.module.NetworkModule;

/**
 * Fabric implementation of {@link NetworkModule}.
 *
 * Connection presence is checked via the network handler.
 * Ping is read from the player's {@link PlayerListEntry} which the
 * server populates; returns {@code -1} when unavailable.
 */
public class NetworkModuleFabric implements NetworkModule {

    private final MinecraftClient mc;

    public NetworkModuleFabric(MinecraftClient mc) {
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
