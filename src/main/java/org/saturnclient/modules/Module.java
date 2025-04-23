package org.saturnclient.modules;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.ui.Textures;

import net.minecraft.util.Identifier;

public abstract class Module {
    private final String name;
    private final String namespace;
    private final ConfigManager configManager;

    public Module(String name, String namespace, NamedProperty<?>... props) {
        this.name = name;
        this.namespace = namespace;

        configManager = new ConfigManager(name);

        for (NamedProperty<?> prop : props) {
            configManager.property(prop.name, prop.prop);
        }
    }

    public Module(ConfigManager config, String name, String namespace) {
        this.name = name;
        this.namespace = namespace;

        configManager = config;
    }

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean e);

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
