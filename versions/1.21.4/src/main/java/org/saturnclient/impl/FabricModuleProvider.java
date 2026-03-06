package org.saturnclient.impl;

import org.saturnclient.impl.modules.ArmorDisplayFabric;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModManager.ModuleProvider;
import org.saturnclient.modules.mods.*;

public class FabricModuleProvider implements ModuleProvider {
    public static Module[] MODS = {
            new ArmorDisplay(new ArmorDisplayFabric())
    };

    @Override
    public Module[] getMods() {
        return MODS;
    }
}
