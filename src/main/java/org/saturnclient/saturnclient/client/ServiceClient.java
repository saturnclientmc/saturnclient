package org.saturnclient.saturnclient.client;

import java.net.http.WebSocket;
import java.util.UUID;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import dev.selimaj.session.Session;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ServiceClient {
    private static Session session;
    public static UUID uuid;

    public static boolean authenticate() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> {
            if (session != null)
                try {
                    session.close();
                } catch (Exception e) {
                    SaturnClient.LOGGER.error("Unable to close Saturn session");

                }
        });

        try {
            var mcSession = SaturnClient.client.getSession();
            if (mcSession == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = mcSession.getAccessToken();
            uuid = mcSession.getUuidOrNull();

            SaturnClient.LOGGER.info("Authenticating with UUID: " + accessToken);

            // String cloak = parser.getString("cloak");
            // String hat = parser.getString("hat");

            // for (String availableCloak : parser.getArray("cloaks")) {
            // Cloaks.availableCloaks.add(availableCloak);
            // }

            // for (String availableHat : parser.getArray("hats")) {
            // Hats.availableHats.add(availableHat);
            // }

            // players.put(uuid, new SaturnPlayer(cloak, hat));

            // Cloaks.loadCloak(uuid);

            // playerNames.put(SaturnClient.client.getSession().getUsername(), uuid);

            // afterAuth();

            // startListenerThread();
            // startPingThread();

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }
}
