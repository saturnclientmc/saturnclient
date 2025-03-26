package org.saturnclient.saturnclient.config;

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
import net.minecraft.client.MinecraftClient;
import org.saturnclient.saturnclient.SaturnClient;

public class ConfigManager {

    private static File configFile = new File(
        MinecraftClient.getInstance().runDirectory,
        "saturn.json"
    );
    private static Map<String, Map<String, Property<?>>> properties =
        new HashMap<>();

    private String namespace;

    public ConfigManager(String namespace) {
        this.namespace = namespace;
        properties.put(namespace, new LinkedHashMap<>());
    }

    // Generic method to store any type of property
    public <T> Property<T> property(String name, Property<T> value) {
        properties.get(namespace).put(name, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        try {
            SaturnClient.LOGGER.info("Starting to load config...");
            if (!configFile.exists()) {
                configFile.createNewFile();
                Files.write(configFile.toPath(), "{}".getBytes());
            }

            JsonObject jsonObject = JsonParser.parseString(
                new String(Files.readAllBytes(configFile.toPath()))
            ).getAsJsonObject();

            for (String namespace : properties.keySet()) {
                JsonElement configElement = jsonObject.get(namespace);

                if (configElement == null) continue;

                JsonObject config = jsonObject.get(namespace).getAsJsonObject();

                if (config == null) {
                    SaturnClient.LOGGER.warn(
                        "No config found for namespace: " + namespace
                    );
                    continue;
                }

                Map<String, Property<?>> propertyMap = properties.get(
                    namespace
                );

                for (String propertyName : propertyMap.keySet()) {
                    JsonElement c = config.get(propertyName);

                    if (c == null) {
                        SaturnClient.LOGGER.warn(
                            "No property found in config for " + propertyName
                        );
                        continue;
                    }

                    SaturnClient.LOGGER.info("got prop " + c);
                    Property<?> p = propertyMap.get(propertyName);
                    SaturnClient.LOGGER.info(
                        "setting prop " + propertyName + " to " + p.value
                    );

                    SaturnClient.LOGGER.info("Property type: " + p.getType());
                    if (p.matchesJson(c)) {
                        switch (p.getType()) {
                            case BOOLEAN:
                                ((Property<Boolean>) p).value =
                                    c.getAsBoolean();
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
                    } else {
                        SaturnClient.LOGGER.warn(
                            "Property does not match JSON: " + propertyName
                        );
                    }
                }
            }
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error reading the config file", e);
        }
    }

    public static void save() {
        try {
            SaturnClient.LOGGER.info("Starting to save config...");

            JsonObject jsonObject = new JsonObject();

            // Iterate through all namespaces and their properties
            for (String namespace : properties.keySet()) {
                JsonObject namespaceConfig = new JsonObject();
                Map<String, Property<?>> propertyMap = properties.get(
                    namespace
                );

                // Iterate through each property in the namespace
                for (String propertyName : propertyMap.keySet()) {
                    Property<?> property = propertyMap.get(propertyName);
                    JsonElement propertyValue = null;

                    // Convert the property value based on its type
                    switch (property.getType()) {
                        case BOOLEAN:
                            propertyValue = new JsonPrimitive(
                                (Boolean) property.value
                            );
                            break;
                        case INTEGER:
                            propertyValue = new JsonPrimitive(
                                (Integer) property.value
                            );
                            break;
                        case FLOAT:
                            propertyValue = new JsonPrimitive(
                                (Float) property.value
                            );
                            break;
                        case STRING:
                            propertyValue = new JsonPrimitive(
                                (String) property.value
                            );
                            break;
                        case HEX:
                            propertyValue = new JsonPrimitive(
                                (Integer) property.value
                            );
                            break;
                        default:
                            SaturnClient.LOGGER.warn(
                                "Unknown property type for: " + propertyName
                            );
                            break;
                    }

                    if (propertyValue != null) {
                        namespaceConfig.add(propertyName, propertyValue);
                    }
                }

                // Add the namespace config to the main JSON object
                jsonObject.add(namespace, namespaceConfig);
            }

            // Write the JSON object to the config file
            Files.write(configFile.toPath(), jsonObject.toString().getBytes());
            SaturnClient.LOGGER.info("Config saved successfully.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error saving the config file", e);
        }
    }

    public Map<String, Property<?>> getProperties() {
        return properties.get(namespace);
    }
}
