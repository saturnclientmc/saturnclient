package org.saturnclient.modules.mods;

import java.util.Objects;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.TickModule;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;

public class AutoSprint extends Module implements TickModule {

    private static ConfigManager config = new ConfigManager("Armor Display");

    public AutoSprint() {
        super(config, "Auto Sprint", "auto_sprint");
    }
    
    @Override
    public void tick() {
        if (SaturnClient.client.options.forwardKey.isPressed() && !SaturnClient.client.options.backKey.isPressed() && !SaturnClient.client.player.horizontalCollision) {
                Objects.requireNonNull(SaturnClient.client.player).setSprinting(true);
        }
    }
}
