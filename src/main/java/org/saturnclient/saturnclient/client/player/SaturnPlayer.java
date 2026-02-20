package org.saturnclient.saturnclient.client.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaturnPlayer {
    private static Map<UUID, SaturnPlayer> PLAYERS = new HashMap<>();

    public String cloak = "";
    public String hat = "";

    public SaturnPlayer(String cloak, String hat) {
        this.cloak = cloak;
        this.hat = hat;
    }

    public static SaturnPlayer get(UUID uuid) {
        return PLAYERS.get(uuid);
    }

    public static void set(UUID uuid, SaturnPlayer player) {
        PLAYERS.put(uuid, player);
    }

    public static void player(UUID uuid, SaturnPlayer player) {
        if (PLAYERS.containsKey(uuid)) {
            return;
        }

        // If offline then leave

        PLAYERS.put(uuid, player);
    }
}
