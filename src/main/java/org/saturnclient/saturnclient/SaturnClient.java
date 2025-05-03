package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;

import org.saturnclient.modules.ModManager;
import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.ui2.screens.TitleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;

/**
 * Main class for the Saturn Client mod.
 */
public class SaturnClient implements ModInitializer {

    public static final String MOD_ID = "saturnclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MinecraftClient client;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);
        client = MinecraftClient.getInstance();
        ModManager.init();
        SaturnClientConfig.init();
        ThemeManager.load();
        ConfigManager.load();
        
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> ConfigManager.save());
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (SaturnClientConfig.saturnTitleScreen.value && screen instanceof TitleScreen
            && !(screen instanceof TitleMenu)) {
                client.setScreen(new TitleMenu());
            }
        });
        
        SpecialModelLoaderEvents.LOAD_SCOPE.register(() -> {
            return (resourceManager, location) -> MOD_ID.equals(location.getNamespace());
        });
        
        KeyInputHandler.register();
        if (Auth.authenticate()) {
            Cloaks.initialize();
            Hats.initialize();
            LOGGER.info(MOD_ID + " initialization complete");
        } else {
            LOGGER.error("Failed to authenticate with the server");
        }
    }
}
