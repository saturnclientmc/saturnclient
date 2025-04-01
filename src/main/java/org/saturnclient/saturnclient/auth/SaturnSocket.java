package org.saturnclient.saturnclient.auth;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

public class SaturnSocket {

    private static final String server = "77.247.92.168";
    private static final int port = 8080;

    public static Map<String, SaturnPlayer> players = new HashMap<>();
    public static Map<String, String> playerNames = new HashMap<>();
    public static String uuid;

    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static Thread listenerThread;
    private static volatile boolean running = false;

    @SuppressWarnings("resource")
    public static boolean authenticate() {
        // Register shutdown hook ONCE
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> close());

        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getSession() == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = client.getSession().getAccessToken();
            SaturnClient.LOGGER.info("Authenticating");

            // Establish a persistent connection
            socket = new Socket(server, port);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Send authentication request
            writer.println(accessToken);
            String response = reader.readLine();

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

            playerNames.put(client.getSession().getUsername(), uuid);

            afterAuth();

            startListenerThread();

            return true;
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    private static void startListenerThread() {
        running = true;
        listenerThread = new Thread(() -> {
            try {
                while (running) {
                    String message = reader.readLine();
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
                                Cloaks.setCloakSilent(uuid, cloak);
                            } else {
                                playerNames.entrySet().removeIf(entry -> entry.getValue().equals(uuid));
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                SaturnClient.LOGGER.error("Error reading from server", e);
            } finally {
                close();
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public static void close() {
        running = false;
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
            SaturnClient.LOGGER.info("SaturnApi connection closed.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Failed to close resources", e);
        }
    }

    public static void afterAuth() {
        PlayerTracker.initialize();
    }

    public static void setCloak(String cloakName) {
        try {
            writer.println("set_cloak@cloak=" + cloakName);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void setHat(String hatName) {
        try {
            writer.println("set_hat@hat=" + hatName);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void player(String name, String uuid) {
        try {
            playerNames.put(name, uuid.replaceAll("-", ""));
            writer.println("player@uuid=" + uuid.replaceAll("-", ""));
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }

    public static void joinServer(String ip) {
        try {
            writer.println("join_server@ip=" + ip);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }
}
