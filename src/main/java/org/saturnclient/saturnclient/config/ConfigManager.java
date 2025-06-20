package org.saturnclient.saturnclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.saturnclient.saturnclient.SaturnClient;

public class ConfigManager {
    private static File configFile = new File(
            SaturnClient.client.runDirectory,
            "saturn.json");
    private static Map<String, Map<String, Property<?>>> properties = new HashMap<>();
    private static JsonObject cachedThemeJson = null;

    private Map<String, Property<?>> currentMap;
    private String namespace;

    public ConfigManager(String namespace) {
        currentMap = new LinkedHashMap<>();
        properties.put(namespace, currentMap);
        System.out.println("Created namespace: " + namespace);
    }

    public ConfigManager(ConfigManager config, String namespace) {
        currentMap = new LinkedHashMap<>();
        Property<Map<String, Property<?>>> namespaceProperty = Property.namespace(currentMap);
        config.property(namespace, namespaceProperty);
        // Update the parent's state
        config.currentMap.put(namespace, namespaceProperty);
        System.out.println("Created sub-namespace: " + namespace);
    }

    public void sub(String namespace) {
        Property<Map<String, Property<?>>> namespaceProperty = Property.namespace(currentMap);
        property(namespace, namespaceProperty);
    }

    // Generic method to store any type of property
    public <T> Property<T> property(String name, Property<T> value) {
        System.out.println("Adding property: " + name);
        currentMap.put(name, value);
        loadProp(name, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> void loadProp(String name, Property<T> prop) {
        if (cachedThemeJson != null) {
            JsonElement element = cachedThemeJson.get(namespace);
            if (element != null && element.isJsonObject()) {
                JsonObject theme = element.getAsJsonObject();
                JsonElement value = theme.get(name);
                if (value != null && prop.matchesJson(value)) {
                    switch (prop.getType()) {
                        case BOOLEAN:
                            ((Property<Boolean>) prop).setValue(value.getAsBoolean());
                            break;
                        case INTEGER:
                            ((Property<Integer>) prop).setValue(value.getAsInt());
                            break;
                        case FLOAT:
                            ((Property<Float>) prop).setValue(value.getAsFloat());
                            break;
                        case STRING:
                            ((Property<String>) prop).setValue(value.getAsString());
                            break;
                        case HEX:
                            ((Property<Integer>) prop).setValue(value.getAsInt());
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            load();
        }
    }

    public static void load() {
        try {
            SaturnClient.LOGGER.info("Starting to load config...");
            if (!configFile.exists()) {
                configFile.createNewFile();
                Files.write(configFile.toPath(), "{}".getBytes());
            }

            JsonObject jsonObject = JsonParser.parseString(
                    new String(Files.readAllBytes(configFile.toPath()))).getAsJsonObject();

            for (String namespace : properties.keySet()) {
                SaturnClient.LOGGER.info("Loading namespace: " + namespace);
                JsonElement configElement = jsonObject.get(namespace);

                if (configElement == null)
                    continue;

                JsonObject config = jsonObject.get(namespace).getAsJsonObject();

                if (config == null) {
                    continue;
                }

                Map<String, Property<?>> propertyMap = properties.get(namespace);
                loadProperties(config, propertyMap);
            }
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error reading the config file", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadProperties(JsonObject config, Map<String, Property<?>> propertyMap) {
        SaturnClient.LOGGER.info("Loading properties: " + propertyMap);

        for (String propertyName : propertyMap.keySet()) {
            JsonElement c = config.get(propertyName);

            if (c == null) {
                continue;
            }

            Property<?> p = propertyMap.get(propertyName);
            if (p.matchesJson(c)) {
                if (p.getType() == Property.PropertyType.NAMESPACE) {
                    // Handle nested namespace
                    JsonObject nestedConfig = c.getAsJsonObject();
                    Map<String, Property<?>> nestedProperties = p.getNamespaceValue();
                    loadProperties(nestedConfig, nestedProperties);
                } else {
                    if (p.value instanceof Integer) {
                        ((Property<Integer>) p).value = c.getAsInt();
                    } else if (p.value instanceof String) {
                        ((Property<String>) p).value = c.getAsString();
                    } else if (p.value instanceof Float) {
                        ((Property<Float>) p).value = c.getAsFloat();
                    } else if (p.value instanceof Boolean) {
                        ((Property<Boolean>) p).value = c.getAsBoolean();
                    } else {
                        SaturnClient.LOGGER.warn(
                                "Unknown property type for: " + propertyName);
                    }
                }
            } else {
                SaturnClient.LOGGER.warn(
                        "Property does not match JSON: " + propertyName);
            }
        }
    }

    public static void save() {
        try {
            SaturnClient.LOGGER.info("Starting to save config...");

            JsonObject jsonObject = new JsonObject();

            // Iterate through all namespaces and their properties
            for (String namespace : properties.keySet()) {
                JsonObject namespaceConfig = new JsonObject();
                Map<String, Property<?>> propertyMap = properties.get(namespace);

                // Save properties for this namespace
                saveProperties(namespaceConfig, propertyMap);

                // Add the namespace config to the main JSON object
                jsonObject.add(namespace, namespaceConfig);
            }

            // Format the json so it can easily be edited
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String formattedJson = gson.toJson(jsonObject);

            // Write the JSON object to the config file
            Files.write(configFile.toPath(), formattedJson.getBytes());
            SaturnClient.LOGGER.info("Config saved successfully.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error saving the config file", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void saveProperties(JsonObject config, Map<String, Property<?>> propertyMap) {
        // Iterate through each property in the namespace
        for (String propertyName : propertyMap.keySet()) {
            Property<?> property = propertyMap.get(propertyName);
            JsonElement propertyValue = null;
            // Convert the property value based on its type
            switch (property.getType()) {
                case NAMESPACE:
                    // Handle nested namespace
                    JsonObject nestedConfig = new JsonObject();
                    Map<String, Property<?>> nestedProperties = (Map<String, Property<?>>) property.value;
                    saveProperties(nestedConfig, nestedProperties);
                    propertyValue = nestedConfig;
                    break;
                default:
                    if (property.value instanceof Integer) {
                        propertyValue = new JsonPrimitive((Integer) property.value);
                    } else if (property.value instanceof String) {
                        propertyValue = new JsonPrimitive((String) property.value);
                    } else if (property.value instanceof Float) {
                        propertyValue = new JsonPrimitive((Float) property.value);
                    } else if (property.value instanceof Boolean) {
                        propertyValue = new JsonPrimitive((Boolean) property.value);
                    } else {
                        SaturnClient.LOGGER.warn(
                                "Unknown property type for: " + propertyName);
                    }
                    break;
            }

            if (propertyValue != null) {
                config.add(propertyName, propertyValue);
            }
        }
    }

    public Map<String, Property<?>> getProperties() {
        return currentMap;
    }
}
