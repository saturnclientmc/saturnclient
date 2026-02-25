package org.saturnclient.saturnclient.config.manager;

import com.google.gson.*;
import org.saturnclient.saturnclient.SaturnClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {

    private static final File configFile = new File(SaturnClient.client.runDirectory, "saturn.json");

    // SINGLE ROOT TREE
    private static final Map<String, Property<?>> ROOT = new LinkedHashMap<>();

    private static JsonObject cachedJson = null;

    private final Map<String, Property<?>> currentMap;

    private final String[] path;

    /**
     * Creates a root namespace
     */
    public ConfigManager(String namespace) {
        this.currentMap = new LinkedHashMap<>();
        this.path = new String[] { namespace }; // Path is just ["namespace"]
        ROOT.put(namespace, Property.namespace(currentMap));
    }

    /**
     * Creates a sub-namespace inside a parent
     */
    public ConfigManager(ConfigManager parent, String namespace) {
        this.currentMap = new LinkedHashMap<>();

        // Build new path: parent path + current namespace
        this.path = new String[parent.path.length + 1];
        System.arraycopy(parent.path, 0, this.path, 0, parent.path.length);
        this.path[this.path.length - 1] = namespace;

        parent.property(namespace, Property.namespace(currentMap));
    }

    /**
     * Add a property
     */
    public <T> Property<T> property(String name, Property<T> value) {
        currentMap.put(name, value);
        loadSingleProperty(name, value);
        return value;
    }

    /**
     * Add empty namespace
     */
    public ConfigManager sub(String name) {
        return new ConfigManager(this, name);
    }

    /**
     * Load entire config
     */
    public static void load() {
        SaturnClient.LOGGER.info("Loading config...");

        JsonObject json = loadAndCache();
        if (json == null)
            return;

        loadRecursive(json, ROOT);
    }

    /**
     * Save entire config
     */
    public static void save() {
        SaturnClient.LOGGER.info("Saving config...");

        JsonObject rootJson = new JsonObject();
        saveRecursive(rootJson, ROOT);

        try {
            String formatted = new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(rootJson);

            Files.writeString(configFile.toPath(), formatted);
            SaturnClient.LOGGER.info("Config saved successfully.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error saving config file", e);
        }
    }

    /**
     * Recursive save
     */
    private static void saveRecursive(JsonObject json, Map<String, Property<?>> map) {
        map.forEach((name, prop) -> {
            if (prop.isNamespace()) {
                JsonObject child = new JsonObject();
                saveRecursive(child, prop.getNamespaceMap());
                json.add(name, child);
            } else {
                json.add(name, prop.toJson());
            }
        });
    }

    /**
     * Recursive load
     */
    private static void loadRecursive(JsonObject json, Map<String, Property<?>> map) {
        map.forEach((name, prop) -> {
            JsonElement element = json.get(name);
            if (element == null)
                return;

            if (prop.isNamespace() && element.isJsonObject()) {
                loadRecursive(element.getAsJsonObject(), prop.getNamespaceMap());
            } else {
                prop.loadFromJson(element);
            }
        });
    }

    /**
     * Load only a single property immediately after creation,
     * traversing the JSON tree based on this manager's path.
     */
    private void loadSingleProperty(String name, Property<?> prop) {
        JsonObject source = cachedJson != null ? cachedJson : loadAndCache();
        if (source == null)
            return;

        // Traverse the tree to the correct namespace
        JsonObject currentScope = source;
        for (String segment : path) {
            JsonElement next = currentScope.get(segment);
            if (next != null && next.isJsonObject()) {
                currentScope = next.getAsJsonObject();
            } else {
                return; // Path doesn't exist in config yet
            }
        }

        // Now look for the property inside the correctly nested object
        JsonElement element = currentScope.get(name);
        if (element != null) {
            prop.loadFromJson(element);
        }
    }

    /**
     * Reads file and caches json
     */
    private static JsonObject loadAndCache() {
        try {
            if (!configFile.exists()) {
                Files.writeString(configFile.toPath(), "{}");
                return new JsonObject();
            }

            String json = Files.readString(configFile.toPath());
            cachedJson = JsonParser.parseString(json).getAsJsonObject();
            return cachedJson;

        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error reading config file", e);
            return null;
        }
    }

    public Map<String, Property<?>> getProperties() {
        return currentMap;
    }
}