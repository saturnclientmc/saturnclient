package org.saturnclient.saturnclient.client;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.saturnclient.saturnclient.SaturnClient;

import dev.selimaj.session.Session;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ServiceClient {
    private static Session session;
    public static UUID uuid;

    public static boolean connectTimeout() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Session> task = () -> Session.connect("ws://localhost:3000");

        Future<Session> future = executor.submit(task);

        try {
            session = future.get(10, TimeUnit.SECONDS);
            System.out.println("Connected!");
            return true;
        } catch (TimeoutException e) {
            System.out.println("Connection timed out!");
            future.cancel(true);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
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
                return false;
            }

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

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }
}
