package org.saturnclient.modules.mods;

import java.util.Objects;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;

public class AutoSprint extends Module {
    private static Property<Boolean> enabled = Property.bool(false);

    public AutoSprint() {
        super(
            new ModuleDetails("Auto Sprint", "sprint")
            .description("Makes the player always sprint")
            .tags("Movement")
            .version("v0.1.0"),
            enabled.named("Enabled"));
    }

    @Override
    public void tick() {
        if (SaturnClient.client.player == null || SaturnClient.client.getNetworkHandler() == null) {
            return;
        }
        if (SaturnClient.client.options.forwardKey.isPressed() 
            && !SaturnClient.client.options.backKey.isPressed() 
            && !SaturnClient.client.player.isSneaking() 
            && !SaturnClient.client.player.horizontalCollision 
            && !SaturnClient.client.player.isUsingItem()) {

            Objects.requireNonNull(SaturnClient.client.player).setSprinting(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}
