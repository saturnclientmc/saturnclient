package org.saturnclient.modules;

public class ModuleDetails {
    public String name;
    public String namespace;
    public String description;
    public String version;
    public String[] tags = {};

    public ModuleDetails(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public ModuleDetails name(String name) {
        this.name = name;
        return this;
    }

    public ModuleDetails namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public ModuleDetails description(String description) {
        this.description = description;
        return this;
    }

    public ModuleDetails version(String version) {
        this.version = version;
        return this;
    }

    public ModuleDetails tags(String... tags) {
        this.tags = tags;
        return this;
    }
}
