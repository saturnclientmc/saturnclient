package org.saturnclient.config.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.saturnclient.common.minecraft.MinecraftProvider;

import java.util.Map;

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

    public JsonNode toJson() {

        switch (type) {

            case BOOLEAN:
                return JsonNodeFactory.instance.booleanNode((Boolean) value);

            case INTEGER:
                return JsonNodeFactory.instance.numberNode((Integer) value);

            case FLOAT:
                return JsonNodeFactory.instance.numberNode((Float) value);

            case STRING:
                return JsonNodeFactory.instance.textNode((String) value);

            case HEX:
                return JsonNodeFactory.instance.textNode(
                        String.format("#%08X", (Integer) value));

            case KEYBINDING:
            case SELECT:
                return JsonNodeFactory.instance.numberNode((Integer) value);

            case NAMESPACE:

                ObjectNode nested = JsonNodeFactory.instance.objectNode();

                getNamespaceValue().forEach((k, v) -> {
                    nested.set(k, v.toJson());
                });

                return nested;

            default:
                return JsonNodeFactory.instance.textNode(String.valueOf(value));
        }
    }

    public void loadFromJson(JsonNode element) {

        if (element == null)
            return;

        switch (type) {

            case BOOLEAN:
                if (element.isBoolean())
                    setValue(element.booleanValue());
                break;

            case INTEGER:
                if (element.isInt())
                    setValue(element.intValue());
                else if (element.isTextual())
                    setValue(parseHexToInt(element.textValue()));
                break;

            case FLOAT:
                if (element.isNumber())
                    setValue(element.floatValue());
                break;

            case STRING:
                if (element.isTextual())
                    setValue(element.textValue());
                break;

            case HEX:
                if (element.isTextual())
                    setValue(parseHexToInt(element.textValue()));
                break;

            case NAMESPACE:

                if (element.isObject()) {

                    ObjectNode obj = (ObjectNode) element;

                    getNamespaceValue().forEach((k, v) -> {
                        v.loadFromJson(obj.get(k));
                    });

                }

                break;

            case SELECT:
                if (element.isInt())
                    setSelection(element.intValue());
                break;

            case KEYBINDING:
                if (element.isInt())
                    setValue(element.intValue());
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

    // ---------- Keybindings (no SaturnClient) ----------

    public boolean isKeyPressed(long windowHandle, boolean screenOpen) {

        return (Integer) value != -1 &&
                MinecraftProvider.PROVIDER.isKeyPressed((Integer) value) && !screenOpen;
    }

    public boolean wasKeyPressed(long windowHandle, boolean screenOpen) {

        boolean pressed = isKeyPressed(windowHandle, screenOpen);

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
            throw new IllegalArgumentException(
                    "Hex must be 6 or 8 chars long, got '" + hex + "'");

        return (int) Long.parseLong(hex, 16);
    }

    // ---------- Misc ----------

    public Property<T> copy() {
        return new Property<>(this.value, this.type);
    }

    public boolean isNamespace() {
        return type == PropertyType.NAMESPACE;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Property<?>> getNamespaceMap() {
        return isNamespace() ? (Map<String, Property<?>>) value : null;
    }
}