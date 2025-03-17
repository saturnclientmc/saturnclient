package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.ColorHelper;

import org.saturnclient.saturnclient.cloaks.Cloaks;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Saturn Client mod.
 * Originally created by IIpho3nix and modified for Saturn Client by leo.
 */
public class SaturnClient implements ModInitializer {
    public static final String MOD_ID = "saturnclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static int COLOR = ColorHelper.getArgb(255, 251, 60, 79);
    public static TextRenderer textRenderer = null;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);

        KeyInputHandler.register();
        if (SaturnSocket.authenticate()) {
            Cloaks.initialize();
            LOGGER.info(MOD_ID + " initialization complete");
        } else {
            LOGGER.error("Failed to authenticate with the server");
        }
    }
}
