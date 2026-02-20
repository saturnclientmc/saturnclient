package org.saturnclient.saturnclient.cosmetics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.saturnclient.saturnclient.client.ServiceClient;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;

public class Hats {
    public static final String[] ALL_HATS = { "horns_black", "horns_white", "halo", "halo_black" };
    public static List<String> availableHats = new ArrayList<>();

    public static void initialize() {
        availableHats.add(0, "");
    }

    public static void setHat(UUID uuid, String hatName) {
        setHatSilent(uuid, hatName);
        ServiceClient.setHat(hatName);
    }

    public static void setHatSilent(UUID uuid, String hatName) {
        SaturnPlayer player = SaturnPlayer.get(uuid);
        if (player == null) {
            // Auth.players.put(uuid, new SaturnPlayer("", hatName));
            // TODO: address this behaviour
        } else {
            player.hat = hatName;
        }
    }
}
