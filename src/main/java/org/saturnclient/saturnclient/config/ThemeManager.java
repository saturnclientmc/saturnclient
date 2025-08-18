package org.saturnclient.saturnclient.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.saturnclient.saturnclient.SaturnClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ThemeManager {
    // public static List<ThemeManager> theme = new ArrayList<>();

    private String[] stateList;
    private Map<String, Map<String, Property<?>>> states;
    private Map<String, Property<?>> currentStyling;
    private String namespace;

    public ThemeManager(String namespace, String... states) {
        this.stateList = states;
        this.states = new LinkedHashMap<>();
        this.namespace = namespace;
        currentStyling = new LinkedHashMap<>();
        this.states.put(null, new LinkedHashMap<>());
        for (String s : states) {
            this.states.put(s, new LinkedHashMap<>());
        }
    }

    public void setState(String state) {
        for (Map.Entry<String, Property<?>> tProp : states.get(state).entrySet()) {
            currentStyling.get(tProp.getKey()).setValue(tProp.getValue().value);
        }
    }

    public void applyState(String state) {
    }

    public <T> Property<T> property(String name, Property<T> prop) {
        for (String state : stateList) {
            Property<?> property = Property.from(prop.value);
            states.get(state).put(name, property);
        }

        states.get(null).put(name, Property.from(prop.value));
        currentStyling.put(name, prop);

        return prop;
    }

    public <T> void propertyStateDefault(String state, String name, T value) {
        Property<?> prop = states.get(state).get(name);
        prop.setValue(value, value);
    }
}
