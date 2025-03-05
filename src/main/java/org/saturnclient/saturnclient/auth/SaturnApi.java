package org.saturnclient.saturnclient.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cloaks.Cloaks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SaturnApi {
    private static final String URL = "http://localhost:3000";

    private static String token = null;

    public static Map<String, SaturnPlayer> players = new HashMap<>();
    public static Map<String, String> playerNames = new HashMap<>();

    public static boolean authenticate() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getSession() == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = client.getSession().getAccessToken();

            token = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

            String authUrl = String.format("%s/auth?token=%s", URL, token);

            URL url = URI.create(authUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            boolean success = jsonResponse.get("success").getAsBoolean();
            String uuid = jsonResponse.get("uuid").getAsString().replace("-", "");
            String name = jsonResponse.get("name").getAsString();

            playerNames.put(name, uuid);

            if (success) {
                SaturnClient.LOGGER.info("Successfully authenticated");
            }

            // Update available cloaks list
            Cloaks.availableCloaks.clear();
            jsonResponse.get("cloaks").getAsJsonArray()
                    .forEach(element -> Cloaks.availableCloaks.add(element.getAsString()));

            String cloak = jsonResponse.get("cloak").getAsString();
            if (cloak != null) {
                SaturnClient.LOGGER.info("Setting cloak to " + cloak + " for " + uuid);
                MinecraftClient.getInstance().execute(() -> Cloaks.setCloak(uuid, cloak));
            }

            return success;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    public static void setCloak(String cloak) {
        if (token == null) {
            SaturnClient.LOGGER.error("No token found");
            return;
        }

        try {
            String setCloakUrl = String.format("%s/cloak/%s?token=%s", URL, cloak, token);

            URL url = URI.create(setCloakUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            boolean success = jsonResponse.get("success").getAsBoolean();

            if (success) {
                SaturnClient.LOGGER.info("Successfully set cloak");
            } else {
                SaturnClient.LOGGER.error("Failed to set cloak");
                SaturnClient.LOGGER.error(jsonResponse.get("error").getAsString());
            }

        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to set cloak", e);
        }
    }
}