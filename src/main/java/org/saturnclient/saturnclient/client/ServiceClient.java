package org.saturnclient.saturnclient.client;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import dev.selimaj.session.Session;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ServiceClient {
    private static Session session;
    public static UUID uuid;

    public static boolean connectTimeout() {
        try {
            session = Session.connect("ws://localhost:3000", 0, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

            if (!connectTimeout()) {
                SaturnClient.LOGGER.error("Unable to authenticate: Session Server Timeout");
                return false;
            }

            ServiceMethods.AuthResponse response = session.request(ServiceMethods.Authenticate, accessToken).get();

            for (String availableCloak : response.cloaks()) {
                Cloaks.availableCloaks.add(availableCloak);
            }

            for (String availableHat : response.hats()) {
                Hats.availableHats.add(availableHat);
            }

            SaturnPlayer.set(uuid, new SaturnPlayer(response.cloak(), response.hat()));

            Cloaks.loadCloak(uuid);

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }
}
