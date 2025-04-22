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

import net.minecraft.client.MinecraftClient;

public class Theme {
    private Map<String, Property<?>> currentMap;

    public void setProperty(String name, Property<?> value) {
        currentMap.put(name, value);
    }

    public Property<?> getProperty(String name) {
        return currentMap.get(name);
    }
}
