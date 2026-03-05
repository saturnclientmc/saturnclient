package org.saturnclient.config.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // SINGLE ROOT TREE
    private static final Map<String, Property<?>> ROOT = new LinkedHashMap<>();

    private static JsonNode cachedJson = null;

    private final Map<String, Property<?>> currentMap;

    private final String[] path;

    private File configFile;

    /**
     * Creates a root namespace
     */
    public ConfigManager(File configFile, String namespace) {
        this.configFile = configFile;
        this.currentMap = new LinkedHashMap<>();
        this.path = new String[] { namespace };

        ROOT.put(namespace, Property.namespace(currentMap));
    }

    /**
     * Creates a sub-namespace inside a parent
     */
    public ConfigManager(ConfigManager parent, String namespace) {
        this.configFile = parent.configFile;
        this.currentMap = new LinkedHashMap<>();

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
    public void load() {
        JsonNode json = loadAndCache();
        if (json == null || !json.isObject())
            return;

        loadRecursive((ObjectNode) json, ROOT);
    }

    /**
     * Save entire config
     */
    public void save() {

        ObjectNode rootJson = MAPPER.createObjectNode();
        saveRecursive(rootJson, ROOT);

        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(configFile, rootJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recursive save
     */
    private static void saveRecursive(ObjectNode json, Map<String, Property<?>> map) {
        map.forEach((name, prop) -> {
            if (prop.isNamespace()) {

                ObjectNode child = json.objectNode();
                saveRecursive(child, prop.getNamespaceMap());

                json.set(name, child);

            } else {

                json.set(name, prop.toJson());

            }
        });
    }

    /**
     * Recursive load
     */
    private static void loadRecursive(ObjectNode json, Map<String, Property<?>> map) {

        map.forEach((name, prop) -> {

            JsonNode element = json.get(name);
            if (element == null)
                return;

            if (prop.isNamespace() && element.isObject()) {

                loadRecursive((ObjectNode) element, prop.getNamespaceMap());

            } else {

                prop.loadFromJson(element);

            }

        });
    }

    /**
     * Load only a single property immediately after creation
     */
    private void loadSingleProperty(String name, Property<?> prop) {

        JsonNode source = cachedJson != null ? cachedJson : loadAndCache();
        if (source == null || !source.isObject())
            return;

        JsonNode currentScope = source;

        for (String segment : path) {

            JsonNode next = currentScope.get(segment);

            if (next != null && next.isObject()) {
                currentScope = next;
            } else {
                return;
            }

        }

        JsonNode element = currentScope.get(name);

        if (element != null) {
            prop.loadFromJson(element);
        }
    }

    /**
     * Reads file and caches json
     */
    private JsonNode loadAndCache() {

        try {

            if (!configFile.exists()) {

                Files.writeString(configFile.toPath(), "{}");
                cachedJson = MAPPER.createObjectNode();
                return cachedJson;

            }

            cachedJson = MAPPER.readTree(configFile);
            return cachedJson;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Map<String, Property<?>> getProperties() {
        return currentMap;
    }
}