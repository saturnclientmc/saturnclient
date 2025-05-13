package org.saturnclient.saturnclient.config;

public class NamedProperty<T> {
    public String name;
    public Property<T> prop;

    public NamedProperty(String name, Property<T> value) {
        this.name = name;
        this.prop = value;
    }
}
