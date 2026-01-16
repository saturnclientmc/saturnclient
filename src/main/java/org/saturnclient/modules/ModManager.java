package org.saturnclient.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.ArrayList;
import java.util.List;

import org.saturnclient.modules.mods.*;

public class ModManager {
    public static final List<Module> ENABLED_MODS = new ArrayList<>();
    public static final Module[] ALL_MODS = {
            new Crosshair(),
            new AutoSprint(),
            new ArmorDisplay(),
            new Fps(),
            new StatusEffects(),
            new Coordinates(),
            new Freelook(),
            new Keystrokes(),
            new Fullbright(),
            new NoFog()
    };

    public static void init() {
        // Pre-filter enabled modules to avoid checking every tick
        updateEnabledModules();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Only iterate over enabled modules
            for (Module m : ENABLED_MODS) {
                m.tick();
            }
        });
    }

    public static synchronized void updateEnabledModules() {
        ENABLED_MODS.clear();
        for (Module m : ALL_MODS) {
            if (m.isEnabled()) {
                ENABLED_MODS.add(m);
            }
        }
    }

    public static Module[] getAllMods() {
        return ALL_MODS;
    }

    public static void refreshEnabledModules() {
        updateEnabledModules();
    }
}
