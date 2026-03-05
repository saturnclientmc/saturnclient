package org.saturnclient.modules;

import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.config.manager.Property.NamedProperty;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.util.Identifier;

public abstract class Module {
    private final ModuleDetails details;
    private final ConfigManager configManager;

    public Module(ModuleDetails details, NamedProperty<?>... props) {
        this.details = details;
        configManager = new ConfigManager(details.name);
        for (NamedProperty<?> prop : props) {
            configManager.property(prop.name, prop.prop);
        }
    }

    public void render(RenderScope scope) {
    }

    public void tick() {
    }

    public abstract boolean isEnabled();

    public final void setEnabled(boolean e) {
        onEnabled(e);
        ModManager.updateEnabledModules();
    }

    public abstract void onEnabled(boolean e);

    public final String getName() {
        return details.name;
    }

    public Identifier getIconTexture() {
        return Textures.getModIcon(details.namespace);
    }

    public ConfigManager getConfig() {
        return configManager;
    }

    public String getDescription() {
        return details.description;
    }

    public String[] getTags() {
        return details.tags;
    }

    public String getVersion() {
        return details.version;
    }
}
