package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import org.saturnclient.client.ServiceClient;
import org.saturnclient.cosmetics.Emotes;
import org.saturnclient.cosmetics.Hats;
import org.saturnclient.cosmetics.cloak.Cloaks;
import org.saturnclient.common.provider.Providers;
import org.saturnclient.impl.provider.FabricModuleProvider;
import org.saturnclient.impl.provider.GLFWProviderImpl;
import org.saturnclient.impl.provider.RefConstructorImpl;
import org.saturnclient.impl.provider.SaturnProviderImpl;
import org.saturnclient.modules.ModManager;
import org.saturnclient.config.Config;
import org.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.ui.EntityDrawerImpl;
import org.saturnclient.ui.SaturnScreenFabric;
import org.saturnclient.ui.components.SkinPreview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        Providers.saturn = new SaturnProviderImpl();
        Providers.refConstructor = new RefConstructorImpl();
        Providers.GLFW = new GLFWProviderImpl();
        SkinPreview.DRAWER = new EntityDrawerImpl();

        Config.init();
        ModManager.init(new FabricModuleProvider());

        client.execute(() -> {
            SaturnScreenFabric.preload(client);
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> ConfigManager.save());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (org.saturnclient.modules.Module m : ModManager.ENABLED_MODS) {
                m.tick();
            }
        });

        KeyInputHandler.register();
        Emotes.initialize();
        if (ServiceClient.authenticate()) {
            Cloaks.initialize();
            Hats.initialize();
            LOGGER.info(MOD_ID + " initialization complete");
        } else {
            LOGGER.error("Failed to authenticate with the server");
        }
    }
}
