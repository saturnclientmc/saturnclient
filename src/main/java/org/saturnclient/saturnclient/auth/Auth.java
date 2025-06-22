package org.saturnclient.saturnclient.auth;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.saturnclient.SaturnClient;

public class Auth {
    public static Map<String, SaturnPlayer> players = new HashMap<>();
    public static Map<String, String> playerNames = new HashMap<>();
    public static String uuid;
    private static Thread listenerThread;
    private static Thread pingThread;
    private static volatile boolean running = false;

    @SuppressWarnings("resource")
    public static boolean authenticate() {
        // Register shutdown hook ONCE
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> close());

        try {
            if (SaturnClient.client.getSession() == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = SaturnClient.client.getSession().getAccessToken();
            SaturnClient.LOGGER.info("Authenticating");

            Network.init();

            Network.write(accessToken);

            String response = Network.read();

            SaturnParser parser = new SaturnParser(response);

            uuid = parser.getString("uuid");
            String cloak = parser.getString("cloak");
            String hat = parser.getString("hat");

            for (String availableCloak : parser.getArray("cloaks")) {
                Cloaks.availableCloaks.add(availableCloak);
            }

            for (String availableHat : parser.getArray("hats")) {
                Hats.availableHats.add(availableHat);
            }

            players.put(uuid, new SaturnPlayer(cloak, hat));

            Cloaks.loadCloak(uuid);

            playerNames.put(SaturnClient.client.getSession().getUsername(), uuid);

            afterAuth();

            startListenerThread();
            startPingThread();

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    private static void startListenerThread() {
        running = true;
        listenerThread = new Thread(() -> {
            try {
                while (running) {
                    String message = Network.read();
                    if (message == null) {
                        SaturnClient.LOGGER.error("Connection closed");
                        running = false;
                        break;
                    }

                    SaturnClient.LOGGER.info("Received: " + message);

                    SaturnParser parser = new SaturnParser(message);

                    switch (parser.method) {
                        case "player":
                            String uuid = parser.getString("uuid");
                            if (parser.getBool("saturn")) {
                                String cloak = parser.getString("cloak");
                                String hat = parser.getString("hat");
                                players.put(uuid, new SaturnPlayer(cloak, hat));
                                Cloaks.loadCloak(uuid);
                            } else {
                                playerNames.entrySet().removeIf(entry -> entry.getValue().equals(uuid));
                            }
                            break;
                        
                        case "successful_transaction":
                            String cloak = parser.getStringOrNull("cloak");
                            String hat = parser.getStringOrNull("hat");
                            if (cloak != null) {
                                Cloaks.availableCloaks.add(cloak);
                            }
                            if (hat != null) {
                                Hats.availableHats.add(hat);
                            }

                        default:
                            if (parser.error != null) {
                                SaturnClient.LOGGER.error("Error from the server: " + parser.error);
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                SaturnClient.LOGGER.error("Error reading from server", e);
            } finally {
                close();
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private static void startPingThread() {
        pingThread = new Thread(() -> {
            try {
                while (running && !Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(25000); // 25 seconds
                        if (running) {
                            Network.write("ping");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        break; // Exit the loop on interruption
                    }
                }
            } catch (Exception e) {
                if (!(e instanceof InterruptedException)) {
                    SaturnClient.LOGGER.error("Error in ping thread", e);
                }
            }
        });

        pingThread.setDaemon(true);
        pingThread.start();
    }

    public static void close() {
        running = false;
        if (pingThread != null) {
            pingThread.interrupt();
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
        try {
            close();
            SaturnClient.LOGGER.info("SaturnApi connection closed.");
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to close resources", e);
        }
    }

    public static void afterAuth() {
        PlayerTracker.initialize();
    }

    public static void setCloak(String cloakName) {
        try {
            Network.write("set_cloak@cloak=" + cloakName);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void setHat(String hatName) {
        try {
            Network.write("set_hat@hat=" + hatName);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void player(String name, String uuid) {
        try {
            playerNames.put(name, uuid.replaceAll("-", ""));
            Network.write("player@uuid=" + uuid.replaceAll("-", ""));
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void sendReload() {
        for (Map.Entry<String, String> player : playerNames.entrySet()) {
            if (players.containsKey(player.getValue()) && !player.getValue().equals(uuid)) {
                SaturnClient.client.player.networkHandler
                        .sendChatCommand("msg " + player.getKey() + " "
                                + "$SATURN_RELOAD if you are seeing this as a player, please report this to https://github.com/saturnclientmc/saturnclient/issues");
                ;
            }
        }
    }

    public static void sendEmote(String emote) {
        String uuid = SaturnClient.client.player.getUuidAsString().replace("-", "");
        for (Map.Entry<String, String> player : playerNames.entrySet()) {
            if (players.containsKey(player.getValue()) && !player.getValue().equals(uuid)) {
                SaturnClient.client.player.networkHandler
                        .sendChatCommand("msg " + player.getKey() + " "
                                + "$SATURN_EMOTE&@"+uuid+"&@"+emote+"&@ if you are seeing this as a player, please report this to https://github.com/saturnclientmc/saturnclient/issues");
                ;
            }
        }
    }

    public static void cancelEmote() {
        String uuid = SaturnClient.client.player.getUuidAsString().replace("-", "");
        for (Map.Entry<String, String> player : playerNames.entrySet()) {
            if (players.containsKey(player.getValue()) && !player.getValue().equals(uuid)) {
                SaturnClient.client.player.networkHandler
                        .sendChatCommand("msg " + player.getKey() + " "
                                + "$SATURN_CANCEL_EMOTE&@"+uuid+"&@ if you are seeing this as a player, please report this to https://github.com/saturnclientmc/saturnclient/issues");
                ;
            }
        }
    }

    public static void buyCloak(String name) {
        try {
            Network.write("buy_cloak@cloak=" + name);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }
}
