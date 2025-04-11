package org.saturnclient.saturnclient.auth;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

public class PlayerTracker {
    private static Set<UUID> trackedPlayers = new HashSet<>();

    public static void initialize() {
        // When client joins a server, reset tracked players
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            trackedPlayers.clear();
            client.execute(() -> {
                checkForNewPlayers(MinecraftClient.getInstance());
            });
        });

        // Check for new players every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> checkForNewPlayers(client));
    }

    private static void checkForNewPlayers(MinecraftClient client) {
        if (client.getNetworkHandler() == null)
            return;

        for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {
            UUID playerUUID = player.getProfile().getId();

            if (!trackedPlayers.contains(playerUUID)) {
                // A new player has joined
                onPlayerJoin(client, player);
                trackedPlayers.add(playerUUID);
            }
        }
    }

    private static void onPlayerJoin(MinecraftClient client, PlayerListEntry player) {
        GameProfile profile = player.getProfile();
        String uuid = profile.getId().toString();
        if (uuid != SaturnSocket.uuid)
            SaturnSocket.player(profile.getName(), uuid);
    }
}
