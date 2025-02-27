package org.auraclient.auraclient;

import org.auraclient.auraclient.cloaks.Cloaks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* Massive thank you to IIpho3nix for making this mod, i (leo) have modified it to fit Aura Client
*/

public class AuraClient implements ModInitializer {
    public static final String MOD_ID = "auraclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Cloaks.initialize();
    }

    public static void tick() {
        Cloaks.tick();
    }
}
