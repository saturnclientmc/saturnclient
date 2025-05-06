package org.saturnclient.modules;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.util.Identifier;

public abstract class Module {
    private final String name;
    private final String namespace;
    private final String description;
    private final ConfigManager configManager;

    public Module(String name, String namespace, String description, NamedProperty<?>... props) {
        this.name = name;
        this.namespace = namespace;
        this.description = description;

        configManager = new ConfigManager(name);

        for (NamedProperty<?> prop : props) {
            configManager.property(prop.name, prop.prop);
        }
    }

    public Module(ConfigManager config, String name, String namespace, String description) {
        this.name = name;
        this.namespace = namespace;
        this.description = description;

        configManager = config;
    }

    public void render(RenderScope scope) {}
    public void tick() {}

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

    public String getDescription() {
        return description;
    }
}
