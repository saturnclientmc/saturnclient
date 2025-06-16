package org.saturnclient.saturnclient.config;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class Property<T> {

    public enum PropertyType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
        HEX,
        NAMESPACE,
        SELECT,
    }

    public T value;
    public boolean isReset = false;
    private T defaultValue;
    private String[] availableValues;
    private PropertyType type;

    public Property(T value) {
        this.value = value;
        this.defaultValue = value;
        if (value instanceof Boolean) {
            type = PropertyType.BOOLEAN;
        } else if (value instanceof Integer) {
            type = PropertyType.INTEGER;
        } else if (value instanceof Float) {
            type = PropertyType.FLOAT;
        } else if (value instanceof String) {
            type = PropertyType.STRING;
        } else if (valueIsNamespace(value)) {
            type = PropertyType.NAMESPACE;
        }
    }

    public Property(T value, PropertyType type) {
        this.value = value;
        this.defaultValue = value;
        this.type = type;
    }

    public static Property<Integer> select(Integer value, String... availableValues) {
        Property<Integer> property = new Property<>(value, PropertyType.SELECT);
        property.availableValues = availableValues;
        return property;
    }

    public static Property<Integer> font(int value) {
        return select(value, "Default", "Inter", "Inter bold");
    }

    @SuppressWarnings("unchecked")
    public static <T> Property<T> namespace(Map<String, Property<?>> value) {
        Property<T> property = new Property<>((T) value, PropertyType.NAMESPACE);
        property.type = PropertyType.NAMESPACE;
        return property;
    }

    public void next() {
        if ((Integer) value < availableValues.length-1) setValue((Integer)value+1);
        else setValue(0);
    }

    public void prev() {
        if ((Integer) value > 0) setValue((Integer)value-1);
        else setValue(availableValues.length - 1);
    }

    public void setSelection(int selection) {
        if (selection >= 0 && selection < availableValues.length && type == PropertyType.SELECT) {
            setValue(selection);
        }
    }

    public String getSelection() {
        if (type == PropertyType.SELECT) {
            return availableValues[(Integer) value];
        } else {
            return null;
        }
    }

    public void reset() {
        value = defaultValue;
        isReset = true;
    }

    public int getHexString() {
        if (value instanceof String) {
            String str = (String) value;
            if (str.startsWith("0x") || str.startsWith("0X")) {
                try {
                    return Integer.parseInt(str.substring(2), 16);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "Invalid hexadecimal integer format");
                }
            }
        }
        throw new IllegalStateException(
                "Property does not contain a valid hex integer");
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

    public boolean matchesJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (value instanceof Boolean && primitive.isBoolean())
                return true;
            if (value instanceof Integer && primitive.isNumber())
                return true;
            if (value instanceof Float && primitive.isNumber())
                return true;
            if (value instanceof String && primitive.isString())
                return true;

            // Check if JSON string represents a valid hex integer
            if (value instanceof String && primitive.isString()) {
                String str = primitive.getAsString();
                return str.matches("0x[0-9A-Fa-f]+");
            }
        } else if (element.isJsonObject()) {
            return isNamespace(value);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public boolean isNamespace(Object obj) {
        return type == PropertyType.NAMESPACE && value instanceof Map;
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
}
