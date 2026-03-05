package org.saturnclient.saturnclient.config.manager;

import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Property<T> {
    public static class NamedProperty<T> {
        public String name;
        public Property<T> prop;

        public NamedProperty(String name, Property<T> value) {
            this.name = name;
            this.prop = value;
        }
    }

    public enum PropertyType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
        HEX,
        NAMESPACE,
        SELECT,
        KEYBINDING,
    }

    public T value;
    public boolean isReset = false;
    private T defaultValue;
    private String[] availableValues;
    private final PropertyType type;
    private boolean wasPressedLastTick = false;

    private Property(T value, PropertyType type) {
        this.value = value;
        this.defaultValue = value;
        this.type = type;
    }

    // ---------- Factory Methods ----------
    public static <T> Property<T> from(T value) {
        if (value instanceof Boolean)
            return new Property<>(value, PropertyType.BOOLEAN);
        if (value instanceof Integer)
            return new Property<>(value, PropertyType.INTEGER);
        if (value instanceof Float)
            return new Property<>(value, PropertyType.FLOAT);
        if (value instanceof String)
            return new Property<>(value, PropertyType.STRING);
        if (valueIsNamespace(value))
            return new Property<>(value, PropertyType.NAMESPACE);
        return null;
    }

    public static Property<Integer> font(int value) {
        return select(value, "Default", "Inter", "Inter bold");
    }

    public static Property<Integer> select(Integer value, String... availableValues) {
        Property<Integer> property = new Property<>(value, PropertyType.SELECT);
        property.availableValues = availableValues;
        return property;
    }

    public static Property<Map<String, Property<?>>> namespace(Map<String, Property<?>> value) {
        return new Property<>(value, PropertyType.NAMESPACE);
    }

    public static Property<Boolean> bool(boolean value) {
        return new Property<>(value, PropertyType.BOOLEAN);
    }

    public static Property<Integer> integer(int value) {
        return new Property<>(value, PropertyType.INTEGER);
    }

    public static Property<Float> floatProp(float value) {
        return new Property<>(value, PropertyType.FLOAT);
    }

    public static Property<String> string(String value) {
        return new Property<>(value, PropertyType.STRING);
    }

    public static Property<Integer> color(int value) {
        return new Property<>(value, PropertyType.HEX);
    }

    public static Property<Integer> keybinding(int value) {
        return new Property<>(value, PropertyType.KEYBINDING);
    }

    // ---------- Select Helpers ----------
    public void next() {
        if (type == PropertyType.SELECT) {
            int i = (Integer) value;
            setValue((i < availableValues.length - 1) ? i + 1 : 0);
        }
    }

    public void prev() {
        if (type == PropertyType.SELECT) {
            int i = (Integer) value;
            setValue((i > 0) ? i - 1 : availableValues.length - 1);
        }
    }

    public void setSelection(int selection) {
        if (type == PropertyType.SELECT && selection >= 0 && selection < availableValues.length) {
            setValue(selection);
        }
    }

    public String getSelection() {
        return (type == PropertyType.SELECT) ? availableValues[(Integer) value] : null;
    }

    // ---------- Lifecycle ----------
    public void reset() {
        value = defaultValue;
        isReset = true;
    }

    public PropertyType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Property<?>> getNamespaceValue() {
        if (type == PropertyType.NAMESPACE && value instanceof Map) {
            return (Map<String, Property<?>>) value;
        }
        throw new IllegalStateException("Property is not a namespace");
    }

    // ---------- JSON Serialization ----------
    /*
     * BOOLEAN,
     * INTEGER,
     * FLOAT,
     * STRING,
     * HEX,
     * NAMESPACE,
     * SELECT,
     * KEYBINDING,
     */
    public JsonElement toJson() {
        switch (type) {
            case BOOLEAN:
                return new JsonPrimitive((Boolean) value);
            case INTEGER:
                return new JsonPrimitive((Integer) value);
            case FLOAT:
                return new JsonPrimitive((Float) value);
            case NAMESPACE:
                JsonObject nested = new JsonObject();
                getNamespaceValue().forEach((k, v) -> nested.add(k, v.toJson()));
                return nested;
            case HEX:
                return new JsonPrimitive(String.format("#%08X", (Integer) value));
            case KEYBINDING:
                return new JsonPrimitive((Integer) value);
            case SELECT:
                return new JsonPrimitive((Integer) value);
            default:
                return new JsonPrimitive(String.valueOf(value));
        }
    }

    public void loadFromJson(JsonElement element) {
        if (element == null)
            return;

        switch (type) {
            case BOOLEAN:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean())
                    setValue(element.getAsBoolean());
                break;
            case INTEGER:
                if (element.isJsonPrimitive()) {
                    JsonPrimitive p = element.getAsJsonPrimitive();
                    if (p.isNumber())
                        setValue(p.getAsInt());
                    else if (p.isString())
                        setValue(parseHexToInt(p.getAsString()));
                }
                break;
            case FLOAT:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
                    setValue(element.getAsFloat());
                break;
            case STRING:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
                    setValue(element.getAsString());
                break;
            case HEX:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
                    setValue(parseHexToInt(element.getAsString()));
                break;
            case NAMESPACE:
                if (element.isJsonObject()) {
                    JsonObject obj = element.getAsJsonObject();
                    getNamespaceValue().forEach((k, v) -> v.loadFromJson(obj.get(k)));
                }
                break;
            case SELECT:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
                    setSelection(element.getAsInt());
                break;
            case KEYBINDING:
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
                    setValue(element.getAsInt());
                break;
        }
    }

    // ---------- Utility ----------
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static boolean valueIsNamespace(Object obj) {
        return obj instanceof Map;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (T) value;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value, Object defaultValue) {
        this.value = (T) value;
        this.defaultValue = (T) defaultValue;
    }

    public NamedProperty<T> named(String name) {
        return new NamedProperty<>(name, this);
    }

    // ---------- Keybindings ----------
    public boolean isKeyPressed() {
        return (Integer) value != -1 &&
                GLFW.glfwGetKey(SaturnClient.client.getWindow().getHandle(), (Integer) value) == GLFW.GLFW_PRESS &&
                SaturnClient.client.currentScreen == null;
    }

    public boolean wasKeyPressed() {
        boolean pressed = isKeyPressed();
        boolean result = pressed && !wasPressedLastTick;
        wasPressedLastTick = pressed;
        return result;
    }

    // ---------- HEX ----------
    public static int parseHexToInt(String hex) {
        hex = hex.replace("#", "");
        if (hex.length() == 6)
            hex = "FF" + hex;
        if (hex.length() != 8)
            throw new IllegalArgumentException("Hex must be 6 or 8 chars long, got \'" + hex + "\'");
        return (int) Long.parseLong(hex, 16);
    }

    // Misc
    public Property<T> copy() {
        return new Property<>(this.value, this.type);
    }

    public void load(String name, JsonElement element) {
        if (element != null && element.isJsonObject()) {
            load(name, element.getAsJsonObject());
        }
    }

    public void load(String name, JsonObject element) {
        JsonElement value = element.get(name);
        if (value != null)
            this.loadFromJson(value);
    }

    public boolean isNamespace() {
        return type == PropertyType.NAMESPACE;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Property<?>> getNamespaceMap() {
        return isNamespace() ? (Map<String, Property<?>>) (Object) value : null;
    }
}
