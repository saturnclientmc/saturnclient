package org.saturnclient.saturnclient.client;

import java.util.UUID;

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
            String username = mcSession.getUsername();

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

            SaturnPlayer.set(uuid, username, new SaturnPlayer(uuid, username, response.cloak(), response.hat()));

            Cloaks.loadCloak(uuid);

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    public static void setCloak(String cloak) {
        try {
            session.request(ServiceMethods.SetCloak, cloak).get();
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to set cloak (service): ", e);
        }
    }

    public static void setHat(String hat) {
        try {
            session.request(ServiceMethods.SetHat, hat).get();
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to set hat (service): ", e);
        }
    }

    public static void buyCloak(String cloak) {
        try {
            session.request(ServiceMethods.BuyCloak, cloak).get();
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to buy cloak (service): ", e);
        }
    }

    public static void buyHat(String hat) {
        try {
            session.request(ServiceMethods.BuyHat, hat).get();
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to buy hat (service): ", e);
        }
    }
}
