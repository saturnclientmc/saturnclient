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
            new HealthDisplay(new HealthDisplayFabric()),
            new Keystrokes(new KeystrokesFabric()),

            new Clock(),

            new Fullbright(),
            new Nametags(new NametagsFabric()),
            new NoFog(),
            new Ping(new PingFabric()),
            new Speedometer(new SpeedometerFabric()),
            new StatusEffects(new StatusEffectsFabric()),
            new Tps(),
            new Zoom()
    };

    @Override
    public Module[] getMods() {
        return MODS;
    }
}