package org.auraclient.auraclient.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.cloaks.Cloaks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuraApi {
    private static final String URL = "http://localhost:3000";

    private static String token = null;
    private static String uuid = null;

    public static boolean authenticate() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getSession() == null) {
                AuraClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = client.getSession().getAccessToken();

            token = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

            String authUrl = String.format("%s/auth?token=%s", URL, token);

            AuraClient.LOGGER.info("Authenticating with URL: " + authUrl);

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
            uuid = jsonResponse.get("uuid").getAsString();

            if (success) {
                AuraClient.LOGGER.info("Successfully authenticated");
            }

            // Update available cloaks list
            Cloaks.availableCloaks.clear();
            jsonResponse.get("cloaks").getAsJsonArray()
                    .forEach(element -> Cloaks.availableCloaks.add(element.getAsString()));

            String cloak = jsonResponse.get("cloak").getAsString();
            if (cloak != null) {
                AuraClient.LOGGER.info("Setting cloak to " + cloak + " for " + uuid);
                Cloaks.playerCapes.put(uuid, cloak);
            }

            return success;

        } catch (Exception e) {
            AuraClient.LOGGER.error("Authentication failed", e);
            return false;
        }
    }

    public static void setCloak(String cloak) {
        if (token == null) {
            AuraClient.LOGGER.error("No token found");
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
                AuraClient.LOGGER.info("Successfully set cloak");
            } else {
                AuraClient.LOGGER.error("Failed to set cloak");
                AuraClient.LOGGER.error(jsonResponse.get("error").getAsString());
            }

        } catch (Exception e) {
            AuraClient.LOGGER.error("Failed to set cloak", e);
        }
    }
}