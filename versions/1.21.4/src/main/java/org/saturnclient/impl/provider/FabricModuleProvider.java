package org.saturnclient.impl.provider;

import org.saturnclient.feature.Feature;
import org.saturnclient.feature.FeatureManager.ModuleProvider;
import org.saturnclient.feature.features.*;
import org.saturnclient.impl.modules.*;

public class FabricModuleProvider implements ModuleProvider {

    public static final Feature[] MODS = {
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
    public Feature[] getMods() {
        return MODS;
    }
}