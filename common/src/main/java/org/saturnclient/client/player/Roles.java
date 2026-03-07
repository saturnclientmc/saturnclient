package org.saturnclient.client.player;

import java.util.Map;
import java.util.UUID;

import org.saturnclient.config.Config;

public class Roles {
    static enum Role {
        OWNER,
        ADMIN,
        PARTNER,
        CONTRIBUTOR
    }

    private static final Map<UUID, Role> ROLES = Map.of(
            UUID.fromString("d362a042-28bd-49e1-a807-ae74dbe8aba9"),
            Role.OWNER,
            UUID.fromString("d8dc7023-179b-4460-bcf6-493fbf1289a6"),
            Role.CONTRIBUTOR);

    /*
     * Gets the icon color of a specific individual, here are the different colors
     * 
     * - Owner: Dark Red
     * - Admin: Red
     * - Partners: Gold
     * - Contributor: Aqua
     * - Other/player: White
     */
    public static int getIconColor(UUID uuid) {
        Role role = ROLES.get(uuid);

        if (role == null) {
            return 0xFFFFFF;
        }

        switch (role) {
            case OWNER:
                return 0xAA0000;

            case ADMIN:
                return 0xFF5555;

            case PARTNER:
                return 0xFFAA00;

            case CONTRIBUTOR:
                return 0x55FFFF;

            default:
                return 0xFFFFFF;
        }
    }

    public static String getSaturnIndicator() {
        return Config.realisticLogo.value ? "\uE003" : "\uE002";
    }
}
