package org.saturnclient.modules;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.Textures;

import net.minecraft.util.Identifier;

public abstract class Module {
    public static Property<Boolean> enabled;
    private final String name;
    private final String namespace;
    private final ConfigManager configManager;

    public Module(ConfigManager config, String name, String namespace) {
        this.name = name;
        this.namespace = namespace;

        enabled = config.property(
            "Enabled",
            new Property<>(false));

        configManager = config;
    }

    public boolean isEnabled() {
        return enabled.value;
    }

    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    public final String getName() {
        return name;
    }

    public Identifier getIconTexture() {
        return Textures.getModIcon(namespace);
    }

    public ConfigManager getConfig() {
        return configManager;
    }
}
