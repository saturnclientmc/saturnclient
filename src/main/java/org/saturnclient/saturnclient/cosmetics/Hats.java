package org.saturnclient.saturnclient.cosmetics;

import java.util.ArrayList;
import java.util.List;
import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.auth.Auth;

public class Hats {
    public static final String[] ALL_HATS = { "horns_black", "horns_white", "halo", "halo_black" };
    public static List<String> availableHats = new ArrayList<>();

    public static void initialize() {
        availableHats.add(0, "");
    }

    public static void setHat(String uuid, String hatName) {
        setHatSilent(uuid, hatName);
        Auth.setHat(hatName);
    }

    public static void setHatSilent(String uuid, String hatName) {
        SaturnPlayer player = Auth.players.get(uuid);
        if (player == null) {
            Auth.players.put(uuid, new SaturnPlayer("", hatName));
        } else {
            player.hat = hatName;
        }
    }
}
