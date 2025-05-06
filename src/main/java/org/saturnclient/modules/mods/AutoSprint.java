package org.saturnclient.modules.mods;

import java.util.Objects;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;

public class AutoSprint extends Module {
    private static Property<Boolean> enabled = new Property<>(true);

    public AutoSprint() {
        super("Auto Sprint", "sprint", "Makes the player always sprint", enabled.named("Enabled"));
    }

    @Override
    public void tick() {
        if (SaturnClient.client.player == null || SaturnClient.client.getNetworkHandler() == null) {
            return;
        }
        if (SaturnClient.client.options.forwardKey.isPressed() && !SaturnClient.client.options.backKey.isPressed() && !SaturnClient.client.player.isSneaking() &&
            !SaturnClient.client.player.horizontalCollision) {
            Objects.requireNonNull(SaturnClient.client.player).setSprinting(true);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}
