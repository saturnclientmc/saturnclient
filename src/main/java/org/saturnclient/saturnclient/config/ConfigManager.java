package org.saturnclient.saturnclient.config;

import com.google.gson.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import org.saturnclient.saturnclient.SaturnClient;

public class ConfigManager {
    private static final File configFile = new File(SaturnClient.client.runDirectory, "saturn.json");
    private static final Map<String, Map<String, Property<?>>> properties = new HashMap<>();
    private static JsonObject cachedThemeJson = null;

    private final Map<String, Property<?>> currentMap;
    private final String namespace;

    public ConfigManager(String namespace) {
        this.namespace = namespace;
        this.currentMap = new LinkedHashMap<>();
        properties.put(namespace, currentMap);
        System.out.println("Created namespace: " + namespace);
    }

    public ConfigManager(ConfigManager parent, String namespace) {
        this.namespace = namespace;
        this.currentMap = new LinkedHashMap<>();
        Property<Map<String, Property<?>>> nsProp = Property.namespace(currentMap);
        parent.property(namespace, nsProp);
        System.out.println("Created sub-namespace: " + namespace);
    }

    public void sub(String name) {
        property(name, Property.namespace(new LinkedHashMap<>()));
    }

    public <T> Property<T> property(String name, Property<T> value) {
        currentMap.put(name, value);
        loadProp(name, value);
        return value;
    }

    private <T> void loadProp(String name, Property<T> prop) {
        JsonObject source = cachedThemeJson != null ? cachedThemeJson : loadAndCache();
        if (source == null)
            return;

        JsonElement namespaceJson = source.get(namespace);
        if (namespaceJson == null || !namespaceJson.isJsonObject())
            return;

        JsonElement value = namespaceJson.getAsJsonObject().get(name);
        if (value != null)
            prop.loadFromJson(value);
    }

    private static JsonObject loadAndCache() {
        try {
            if (!configFile.exists()) {
                Files.writeString(configFile.toPath(), "{}");
                return new JsonObject();
            }
            String json = Files.readString(configFile.toPath());
            cachedThemeJson = JsonParser.parseString(json).getAsJsonObject();
            return cachedThemeJson;
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error reading config file", e);
            return null;
        }
    }

    public static void load() {
        SaturnClient.LOGGER.info("Loading config...");
        JsonObject json = loadAndCache();
        if (json == null)
            return;

        properties.forEach((namespace, map) -> {
            JsonElement nsElement = json.get(namespace);
            if (nsElement != null && nsElement.isJsonObject()) {
                loadProperties(nsElement.getAsJsonObject(), map);
            }
        });
    }

    private static void loadProperties(JsonObject json, Map<String, Property<?>> props) {
        props.forEach((name, prop) -> {
            JsonElement el = json.get(name);
            if (el != null)
                prop.loadFromJson(el);
        });
    }

    public static void save() {
        SaturnClient.LOGGER.info("Saving config...");
        JsonObject root = new JsonObject();
        properties.forEach((namespace, map) -> {
            JsonObject nsJson = new JsonObject();
            saveProperties(nsJson, map);
            root.add(namespace, nsJson);
        });

        try {
            String formatted = new GsonBuilder().setPrettyPrinting().create().toJson(root);
            Files.writeString(configFile.toPath(), formatted);
            SaturnClient.LOGGER.info("Config saved successfully.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error saving config file", e);
        }
    }

    private static void saveProperties(JsonObject json, Map<String, Property<?>> props) {
        props.forEach((name, prop) -> json.add(name, prop.toJson()));
    }

    public Map<String, Property<?>> getProperties() {
        return currentMap;
    }
}
