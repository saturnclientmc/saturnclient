package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.AutoSprintInterface;
import org.saturnclient.config.manager.Property;

public class AutoSprint extends Module {

    private static Property<Boolean> enabled = Property.bool(false);

    private final AutoSprintInterface minecraft;

    public AutoSprint(AutoSprintInterface minecraft) {
        super(
                new ModuleDetails("Auto Sprint", "sprint")
                        .description("Makes the player always sprint")
                        .tags("Movement")
                        .version("v0.1.0"),
                enabled.named("Enabled"));

        this.minecraft = minecraft;
    }

    @Override
    public void tick() {
        if (!minecraft.hasPlayer() || !minecraft.hasNetwork()) {
            return;
        }

        if (minecraft.isForwardPressed()
                && !minecraft.isBackPressed()
                && !minecraft.isSneaking()
                && !minecraft.hasHorizontalCollision()
                && !minecraft.isUsingItem()) {

            minecraft.setSprinting(true);
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