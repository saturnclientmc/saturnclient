package org.auraclient.auraclient;

import net.fabricmc.api.ModInitializer;
import org.auraclient.auraclient.cloaks.Cloaks;
import org.auraclient.auraclient.event.KeyInputHandler;
import org.auraclient.auraclient.auth.AuraApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Aura Client mod.
 * Originally created by IIpho3nix and modified for Aura Client by leo.
 */
public class AuraClient implements ModInitializer {
    public static final String MOD_ID = "auraclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);
        
        // Attempt authentication before initializing other features
        if (AuraApi.authenticate()) {
            KeyInputHandler.register();
            Cloaks.initialize();
            LOGGER.info(MOD_ID + " initialization complete");
        } else {
            LOGGER.error("Failed to authenticate with the server");
        }
    }
}
