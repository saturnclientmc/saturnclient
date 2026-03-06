package org.saturnclient.impl;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModManager.ModuleProvider;
import org.saturnclient.modules.mods.*;
import org.saturnclient.impl.modules.*;

public class FabricModuleProvider implements ModuleProvider {
    public static final Module[] MODS = {
            new ArmorDisplay(new ArmorDisplayFabric()),
            new AutoSprint(new AutoSprintFabric()),
            new Coordinates(new CoordinatesFabric()),
            new Crosshair(new CrosshairFabric()),
            new DayCounter(new DayCounterFabric()),
            new Fps(new FpsFabric()),
            new Freelook(new FreelookFabric()),
            new Clock(),
    };

    @Override
    public Module[] getMods() {
        return MODS;
    }
}
