package org.saturnclient.saturnclient.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.saturnclient.saturnclient.SaturnClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class ThemeManager {
    
    private static File themeFile = new File(
            SaturnClient.client.runDirectory,
            "saturn.theme.json");
    private static Map<String, Map<String, Property<?>>> properties = new HashMap<>();
    private static JsonObject cachedThemeJson = null;

    // private Map<String, Map<String, Property<?>>> states;
    private Map<String, Property<?>> currentStyling;
    private String state;
    private String[] states;
    private String namespace;

    public ThemeManager(String namespace, String... states) {
        // this.states = new LinkedHashMap<>();
        this.namespace = namespace;
        this.states = states;
        currentStyling = new LinkedHashMap<>();

        properties.put(namespace, new LinkedHashMap<>());

        for (String s : states) {
            properties.put(namespace+"@"+s, new LinkedHashMap<>());
        }
    }

    public void setState(String state) {
        this.state = state;
        if (this.state != null) {
            for (Map.Entry<String, Property<?>> tProp : properties.get(namespace+"@"+state).entrySet()) {
                currentStyling.get(tProp.getKey()).setValue(tProp.getValue().value);
            }
        } else {
            for (Map.Entry<String, Property<?>> tProp : properties.get(namespace).entrySet()) {
                currentStyling.get(tProp.getKey()).setValue(tProp.getValue().value);
            }
        }
    }

    public void applyState(String state) {
        if (this.state != null) {
            for (Map.Entry<String, Property<?>> tProp : properties.get(namespace+"@"+state).entrySet()) {
                Property<?> p = currentStyling.get(tProp.getKey());
                Property<?> currProp = properties.get(namespace).get(tProp.getKey());
                if (p.value.equals(currProp.value)) {
                    p.setValue(tProp.getValue().value);
                }
            }
        } else {
            setState(state);
        }
    }

    // Generic method to store any type of property
    public <T> Property<T> property(String name, Property<T> prop) {
        for (String state : states) {
            Property<?> property = new Property<T>(prop.value);
            properties.get(namespace+"@"+state).put(name, property);
            loadProp(state, name, property);
        }

        loadProp(null, name, prop);
        properties.get(namespace).put(name, new Property<T>(prop.value));
        currentStyling.put(name, prop);

        return prop;
    }

    public <T> void propertyStateDefault(String state, String name, T value) {
        Property<?> prop = properties.get(namespace+"@"+state).get(name);
        prop.setValue(value, value); // sets the current and default value
        loadProp(state, name, prop);
    }

    @SuppressWarnings("unchecked")
    public <T> void loadProp(String state, String name, Property<T> prop) {
        if (cachedThemeJson == null) {
            load();
        }

            JsonElement element = cachedThemeJson.get(state != null ? namespace+"@"+state : namespace);
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
    }

    public static void load() {
        try {
            SaturnClient.LOGGER.info("Starting to load theme...");
            if (!themeFile.exists()) {
                themeFile.createNewFile();
                Files.write(themeFile.toPath(), "{}".getBytes());
            }

            cachedThemeJson = JsonParser.parseString(
                new String(Files.readAllBytes(themeFile.toPath()))).getAsJsonObject();

            SaturnClient.LOGGER.info("Theme loaded");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error reading the theme file", e);
        }
    }

    public static void save() {
        try {
            SaturnClient.LOGGER.info("Starting to save theme...");

            JsonObject jsonObject = new JsonObject();

            // Iterate through all namespaces and their properties
            for (String namespace : properties.keySet()) {
                JsonObject namespaceTheme = new JsonObject();
                Map<String, Property<?>> propertyMap = properties.get(namespace);

                // Save properties for this namespace
                saveProperties(namespaceTheme, propertyMap);

                // Add the namespace theme to the main JSON object
                jsonObject.add(namespace, namespaceTheme);
            }

            // Format the json so it can easily be edited
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String formattedJson = gson.toJson(jsonObject);

            // Write the JSON object to the theme file
            Files.write(themeFile.toPath(), formattedJson.getBytes());
            SaturnClient.LOGGER.info("Theme saved successfully.");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Error saving the theme file", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void saveProperties(JsonObject theme, Map<String, Property<?>> propertyMap) {
        // Iterate through each property in the namespace
        for (String propertyName : propertyMap.keySet()) {
            Property<?> property = propertyMap.get(propertyName);
            JsonElement propertyValue = null;

            if (property.getType() == Property.PropertyType.NAMESPACE) {
                // Handle nested namespace
                JsonObject nestedTheme = new JsonObject();
                Map<String, Property<?>> nestedProperties = (Map<String, Property<?>>) property.value;
                saveProperties(nestedTheme, nestedProperties);
                propertyValue = nestedTheme;
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
                theme.add(propertyName, propertyValue);
            }
        }
    }
}
