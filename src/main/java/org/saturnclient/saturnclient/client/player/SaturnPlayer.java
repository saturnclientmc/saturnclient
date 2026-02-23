package org.saturnclient.saturnclient.client.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.client.ServiceClient;

public class SaturnPlayer {
    private static Map<UUID, SaturnPlayer> PLAYERS = new HashMap<>();
    private static Map<String, UUID> PLAYER_NAMES = new HashMap<>();

    public String cloak = "";
    public String hat = "";
    public final UUID uuid;
    public final String name;

    public SaturnPlayer(UUID uuid, String name, String cloak, String hat) {
        this.cloak = cloak;
        this.hat = hat;
        this.uuid = uuid;
        this.name = name;
    }

    @Nullable
    public static SaturnPlayer get() {
        return PLAYERS.get(ServiceClient.uuid);
    }

    @Nullable
    public static SaturnPlayer get(UUID uuid) {
        return PLAYERS.get(uuid);
    }

    @Nullable
    public static SaturnPlayer get(String name) {
        UUID uuid = PLAYER_NAMES.get(name);
        if (uuid == null)
            return null;
        return PLAYERS.get(uuid);
    }

    public static void set(UUID uuid, String name, SaturnPlayer player) {
        PLAYER_NAMES.put(name, uuid);
        PLAYERS.put(uuid, player);
    }

    public static void player(UUID uuid) {
        if (PLAYERS.containsKey(uuid)) {
            return;
        }

        // If offline then leave

        PLAYERS.put(uuid, null);
    }

    public static String[] getExternalUUIDAsString() {
        return PLAYERS.keySet().stream().filter(id -> !id.equals(ServiceClient.uuid)).map(UUID::toString)
                .toArray(String[]::new);
    }

}
