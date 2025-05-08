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
    public final String[] tags;

    public Module(ModuleDetails details, NamedProperty<?>... props) {
        this.name = details.name;
        this.namespace = details.namespace;
        this.description = details.description;
        this.tags = details.tags;

        configManager = new ConfigManager(name);

        for (NamedProperty<?> prop : props) {
            configManager.property(prop.name, prop.prop);
        }
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
    
    public String[] getTags() {
        return tags;
    }
    
    public String getVersion() {
        return "v0.0.1";
    }
}
