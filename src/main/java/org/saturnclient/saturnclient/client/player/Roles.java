package org.saturnclient.saturnclient.client.player;

import java.util.Map;
import java.util.UUID;

import org.saturnclient.saturnclient.config.SaturnClientConfig;

import net.minecraft.util.Formatting;

public class Roles {
    static enum Role {
        OWNER,
        ADMIN,
        PARTNER,
        CONTRIBUTOR
    }

    private static final Map<UUID, Role> ROLES = Map.of(
            UUID.fromString("d362a042-28bd-49e1-a807-ae74dbe8aba9"),
            Role.OWNER);

    /*
     * Gets the icon color of a specific individual, here are the different colors
     * 
     * - Owner: Dark Red
     * - Admin: Red
     * - Partners: Gold
     * - Contributor: Aqua
     * - Other/player: White
     */
    public static Formatting getIconColor(UUID uuid) {
        Role role = ROLES.get(uuid);

        if (role == null) {
            return Formatting.WHITE;
        }

        switch (role) {
            case OWNER:
                return Formatting.DARK_RED;

            case ADMIN:
                return Formatting.RED;

            case PARTNER:
                return Formatting.GOLD;

            case CONTRIBUTOR:
                return Formatting.AQUA;

            default:
                return Formatting.WHITE;
        }
    }

    public static String getSaturnIndicator() {
        return SaturnClientConfig.realisticLogo.value ? "" : "";
    }
}
