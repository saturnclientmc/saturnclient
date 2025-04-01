package org.saturnclient.saturnclient.cosmetics;

import java.util.ArrayList;
import java.util.List;
import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.auth.SaturnSocket;

public class Hats {
    public static List<String> availableHats = new ArrayList<>();

    public static void initialize() {
        availableHats.add(0, "");
    }

    public static void setHat(String uuid, String hatName) {
        setHatSilent(uuid, hatName);
        SaturnSocket.setHat(hatName);
        SaturnSocket.sendReload();
    }

    public static void setHatSilent(String uuid, String hatName) {
        SaturnPlayer player = SaturnSocket.players.get(uuid);
        if (player == null) {
            SaturnSocket.players.put(uuid, new SaturnPlayer("", hatName));
        } else {
            player.hat = hatName;
        }
    }
}
