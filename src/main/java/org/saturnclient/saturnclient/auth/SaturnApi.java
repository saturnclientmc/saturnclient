package org.saturnclient.saturnclient.auth;

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
    public static Socket socket;

    public static boolean authenticate() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getSession() == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = client.getSession().getAccessToken();

            SaturnClient.LOGGER.error(accessToken);

            try {
                socket = new Socket(server, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(accessToken);
                writer.flush();

                String response = reader.readLine();
                SaturnParser parser = new SaturnParser(response);

                String uuid = parser.getString("uuid");
                String cloak = parser.getString("cloak");

                if (cloak != null) {
                    SaturnClient.LOGGER.info("Setting cloak to " + cloak + " for " + uuid);
                    MinecraftClient.getInstance().execute(() -> Cloaks.setCloak(uuid, cloak));
                }

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                return false;
            }

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    public static void setCloak(String cloak) {
        try {
            // if (success) {
            // SaturnClient.LOGGER.info("Successfully set cloak");
            // } else {
            // SaturnClient.LOGGER.error("Failed to set cloak");
            // SaturnClient.LOGGER.error(jsonResponse.get("error").getAsString());
            // }

        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to set cloak", e);
        }
    }
}