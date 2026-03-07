package org.saturnclient.modules;

import java.util.ArrayList;
import java.util.List;

public class ModManager {
    public static List<Module> ENABLED_MODS = new ArrayList<>();
    public static Module[] MODS = {};

    public static void init(ModuleProvider provider) {
        MODS = provider.getMods();
        updateEnabledModules();
    }

    public static synchronized void updateEnabledModules() {
        ENABLED_MODS.clear();
        for (Module m : MODS) {
            if (m.isEnabled()) {
                ENABLED_MODS.add(m);
            }
        }
    }

    public static Module[] getAllMods() {
        return MODS;
    }

    public interface ModuleProvider {
        Module[] getMods();
    }
}
