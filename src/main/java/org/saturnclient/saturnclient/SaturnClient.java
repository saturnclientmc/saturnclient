package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.ColorHelper;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.saturnmods.ModManager;
import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.animations.FadeIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Saturn Client mod.
 */
public class SaturnClient implements ModInitializer {

    public static final String MOD_ID = "saturnclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ConfigManager config = new ConfigManager("Saturn Client");

    public static Property<Integer> COLOR = config.property(
            "Primary color",
            new Property<Integer>(
                    ColorHelper.getArgb(255, 251, 60, 79),
                    Property.PropertyType.HEX));

    public static Property<Integer> NORMAL = config.property(
            "Default color",
            new Property<Integer>(
                    ColorHelper.getArgb(255, 182, 182, 182),
                    Property.PropertyType.HEX));

    public static int WHITE = ColorHelper.getArgb(255, 255, 255, 255);
    public static TextRenderer textRenderer = null;

    public static int getWhite(float alpha) {
        return ((int) (alpha * 255) << 24) | (NORMAL.value & 0x00FFFFFF);
    }

    public static int getColor(boolean hovering, float alpha) {
        return SaturnUi.getAlpha(hovering ? SaturnClient.COLOR.value : NORMAL.value, alpha);
    }

    public static SaturnAnimation[] getAnimations() {
        return new SaturnAnimation[] { new FadeIn(1) };
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);
        ModManager.init();
        ConfigManager.load();

        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> ConfigManager.save());

        KeyInputHandler.register();
        if (SaturnSocket.authenticate()) {
            Cloaks.initialize();
            Hats.initialize();
            LOGGER.info(MOD_ID + " initialization complete");
        } else {
            LOGGER.error("Failed to authenticate with the server");
        }
    }
}
