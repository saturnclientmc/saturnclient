package org.saturnclient.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.saturnclient.modules.mods.*;

public class ModManager {
    public static Module[] MODS = {
            new Crosshair(),
            new AutoSprint(),
            new ArmorDisplay(),
            new Fps(),
            new PotionDisplay(),
            new Coordinates(),
            new Freelook(),
            new Keystrokes()
    };

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Module m : MODS) {
                if (m.isEnabled()) {
                    m.tick();
                }
            }
        });
    }
}
