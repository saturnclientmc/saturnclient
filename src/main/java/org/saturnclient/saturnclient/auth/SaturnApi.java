package org.saturnclient.saturnclient.auth;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cloaks.Cloaks;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SaturnApi {
    private static final String server = "127.0.0.1";
    private static final int port = 8080;

    public static Map<String, SaturnPlayer> players = new HashMap<>();
    public static Map<String, String> playerNames = new HashMap<>();
    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    public static boolean authenticate() {
        // Register shutdown hook ONCE
        ClientLifecycleEvents.CLIENT_STOPPING.register(c -> close());

        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getSession() == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = client.getSession().getAccessToken();
            SaturnClient.LOGGER.info("Authenticating");

            // Use try-with-resources to ensure proper cleanup
            try (Socket tempSocket = new Socket(server, port);
                    BufferedReader tempReader = new BufferedReader(new InputStreamReader(tempSocket.getInputStream()));
                    PrintWriter tempWriter = new PrintWriter(tempSocket.getOutputStream(), true)) {

                // Assign to class fields
                socket = tempSocket;
                reader = tempReader;
                writer = tempWriter;

                writer.println(accessToken);
                String response = reader.readLine();
                SaturnParser parser = new SaturnParser(response);

                String uuid = parser.getString("uuid");
                String cloak = parser.getString("cloak");

                if (cloak != null) {
                    SaturnClient.LOGGER.info("Setting cloak to " + cloak + " for " + uuid);
                    MinecraftClient.getInstance().execute(() -> Cloaks.setCloak(uuid, cloak));
                }

                return true;
            }

        } catch (IOException e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    public static void close() {
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
            String response = reader.readLine();
            new SaturnParser(response);
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Request failed", e);
        }
    }
}
