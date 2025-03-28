package org.saturnclient.saturnclient.auth;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

public class SaturnSocket {

    private static final String server = "127.0.0.1";
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

            SaturnClient.LOGGER.info(response);

            SaturnParser parser = new SaturnParser(response);

            uuid = parser.getString("uuid");
            String cloak = parser.getString("cloak");
            String hat = parser.getString("hat");

            for (String availableCloak : parser.getArray("cloaks")) {
                SaturnClient.LOGGER.info("Available cloak: " + availableCloak);
                Cloaks.availableCloaks.add(availableCloak);
            }

            players.put(uuid, new SaturnPlayer(cloak, hat));

            playerNames.put(client.getSession().getUsername(), uuid);

            // Start listening for server messages
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
                        running = false;
                        break;
                    }

                    SaturnClient.LOGGER.info("Received: " + message);
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

    public static void setCloak(String cloakName) {
        try {
            writer.println("set_cloak@cloak=" + cloakName);
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }
}
