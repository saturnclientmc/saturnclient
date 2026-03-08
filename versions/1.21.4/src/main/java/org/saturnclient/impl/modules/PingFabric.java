package org.saturnclient.impl.modules;

import org.saturnclient.feature.interfaces.PingInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class PingFabric implements PingInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public int getPing() {
        if (mc.player == null || mc.getNetworkHandler() == null) return 0;
        ClientPlayNetworkHandler handler = mc.getNetworkHandler();
        if (handler.getPlayerListEntry(mc.player.getUuid()) == null) return 0;
        return handler.getPlayerListEntry(mc.player.getUuid()).getLatency();
    }
}