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

    private Map<String, Property<?>> currentMap;

    public ConfigManager(String namespace) {
        currentMap = new LinkedHashMap<>();
        properties.put(namespace, currentMap);
    }

    public ConfigManager(ConfigManager config, String namespace) {
        currentMap = new LinkedHashMap<>();
        Property<Map<String, Property<?>>> namespaceProperty = new Property<>(currentMap,
                Property.PropertyType.NAMESPACE);
        config.property(namespace, namespaceProperty);
        // Update the parent's state
        config.currentMap.put(namespace, namespaceProperty);
    }

    // Generic method to store any type of property
    public <T> Property<T> property(String name, Property<T> value) {
        currentMap.put(name, value);
        return value;
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
                    // Handle regular properties
                    switch (p.getType()) {
                        case BOOLEAN:
                            ((Property<Boolean>) p).value = c.getAsBoolean();
                            break;
                        case INTEGER:
                            ((Property<Integer>) p).value = c.getAsInt();
                            break;
                        case FLOAT:
                            ((Property<Float>) p).value = c.getAsFloat();
                            break;
                        case STRING:
                            ((Property<String>) p).value = c.getAsString();
                            break;
                        case HEX:
                            ((Property<Integer>) p).value = c.getAsInt();
                            break;
                        default:
                            break;
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

            if (property.getType() == Property.PropertyType.NAMESPACE) {
                // Handle nested namespace
                JsonObject nestedConfig = new JsonObject();
                Map<String, Property<?>> nestedProperties = (Map<String, Property<?>>) property.value;
                saveProperties(nestedConfig, nestedProperties);
                propertyValue = nestedConfig;
            } else {
                // Convert the property value based on its type
                switch (property.getType()) {
                    case BOOLEAN:
                        propertyValue = new JsonPrimitive((Boolean) property.value);
                        break;
                    case INTEGER:
                        propertyValue = new JsonPrimitive((Integer) property.value);
                        break;
                    case FLOAT:
                        propertyValue = new JsonPrimitive((Float) property.value);
                        break;
                    case STRING:
                        propertyValue = new JsonPrimitive((String) property.value);
                        break;
                    case HEX:
                        propertyValue = new JsonPrimitive((Integer) property.value);
                        break;
                    default:
                        SaturnClient.LOGGER.warn(
                                "Unknown property type for: " + propertyName);
                        break;
                }
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
