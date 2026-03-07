package org.saturnclient.client.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.saturnclient.common.minecraft.bindings.SaturnClientBindings;

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

    public static SaturnPlayer get() {
        UUID self = SaturnClientBindings.platform().getUuid();
        if (self == null) {
            return null;
        }
        return PLAYERS.get(self);
    }

    public static SaturnPlayer get(UUID uuid) {
        return PLAYERS.get(uuid);
    }

    public static SaturnPlayer get(String name) {
        UUID uuid = PLAYER_NAMES.get(name);
        if (uuid == null)
            return null;
        return PLAYERS.get(uuid);
    }

    public static void set(SaturnPlayer player) {
        PLAYER_NAMES.put(player.name, player.uuid);
        PLAYERS.put(player.uuid, player);
    }

    public static String[] getExternalUUIDAsString() {
        UUID self = SaturnClientBindings.platform().getUuid();
        return PLAYERS.keySet().stream().filter(id -> self == null || !id.equals(self)).map(UUID::toString)
                .toArray(String[]::new);
    }

}
