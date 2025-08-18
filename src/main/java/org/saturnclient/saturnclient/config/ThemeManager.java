package org.saturnclient.saturnclient.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.saturnclient.saturnclient.SaturnClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ThemeManager {
    private static File themeFile = new File(
            SaturnClient.client.runDirectory,
            "saturn.theme.json");
    private static List<ThemeManager> theme = new ArrayList<>();
    private static JsonObject cachedThemeJson = null;

    private Map<String, Map<String, Object>> states;
    private Map<String, Property<?>> currentStyling;
    private String namespace;

    public ThemeManager(String namespace, String... states) {
        this.namespace = namespace;
        this.states = new LinkedHashMap<>();
        this.currentStyling = new LinkedHashMap<>();
        this.states.put(null, new LinkedHashMap<>());
        for (String s : states) {
            this.states.put(s, new LinkedHashMap<>());
        }
        theme.add(this);
    }

    public void setState(String state) {
        for (Map.Entry<String, Object> tProp : states.get(state).entrySet()) {
            currentStyling.get(tProp.getKey()).setValue(tProp.getValue());
        }
    }

    public void applyState(String state) {
        for (Map.Entry<String, Object> tProp : states.get(state).entrySet()) {
            if (!states.get(null).get(tProp.getKey()).equals(tProp.getValue()))
                currentStyling.get(tProp.getKey()).setValue(tProp.getValue());
        }
    }

    public <T> Property<T> property(String name, Property<T> prop) {
        for (Map.Entry<String, Map<String, Object>> state : this.states.entrySet()) {
            Property<?> property = prop.copy();
            if (cachedThemeJson == null) {
                load();
            }
            String stateName = state.getKey();
            property.load(name, cachedThemeJson.get(stateName != null ? namespace + "@" + stateName : namespace));
            state.getValue().put(name, property.value);
        }

        currentStyling.put(name, prop);

        return prop;
    }

    public <T> void propertyStateDefault(String state, String name, T value) {
        Property<?> prop = currentStyling.get(name).copy();
        prop.setValue(value, value);
        if (cachedThemeJson == null) {
            load();
        }
        prop.load(name, cachedThemeJson.get(state != null ? namespace + "@" + state : namespace));
        states.get(state).put(name, prop.value);
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
}
