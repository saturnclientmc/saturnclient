package org.saturnclient.saturnclient.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class Property<T> {

    public enum PropertyType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
    }

    public T value;

    public Property(T value) {
        this.value = value;
    }

    public boolean getBool() {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new IllegalStateException("Property does not contain a boolean");
    }

    public int getInt() {
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new IllegalStateException("Property does not contain an int");
    }

    public float getFloat() {
        if (value instanceof Float) {
            return (float) value;
        }
        throw new IllegalStateException("Property does not contain a float");
    }

    public String getString() {
        if (value instanceof String) {
            return (String) value;
        }
        throw new IllegalStateException("Property does not contain a string");
    }

    public PropertyType getType() {
        if (value instanceof Boolean) {
            return PropertyType.BOOLEAN;
        }
        if (value instanceof Integer) {
            return PropertyType.INTEGER;
        }
        if (value instanceof Float) {
            return PropertyType.FLOAT;
        }
        if (value instanceof String) {
            return PropertyType.STRING;
        }
        return null;
    }

    public boolean matchesJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (value instanceof Boolean && primitive.isBoolean()) return true;
            if (value instanceof Integer && primitive.isNumber()) return true;
            if (value instanceof Float && primitive.isNumber()) return true;
            if (value instanceof String && primitive.isString()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
