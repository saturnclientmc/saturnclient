package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import org.saturnclient.cosmetics.Emotes;
import org.saturnclient.cosmetics.cloak.Cloaks;
import org.saturnclient.cosmetics.hat.Hats;
import org.saturnclient.impl.FabricModuleProvider;
import org.saturnclient.impl.SaturnClientProvider;
import org.saturnclient.modules.ModManager;
import org.saturnclient.saturnclient.client.ServiceClient;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.config.Config;
import org.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui.ElementRenderer;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.RenderScopeImpl;
import org.saturnclient.ui.SaturnScreenFabric;
import org.saturnclient.ui.components.ElementRendererImpl;
import org.saturnclient.ui.elements.ImageTexture;
import org.saturnclient.ui.resources.Textures;
import org.saturnclient.ui.screens.TitleMenu;
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

        MinecraftProvider.PROVIDER = new SaturnClientProvider();

        ElementRenderer.INSTANCE = new ElementRendererImpl();

        Config.init();
        ModManager.init(new FabricModuleProvider());

        client.execute(() -> {
            SaturnScreenFabric.preload(client);
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> ConfigManager.save());

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen != null && !(screen instanceof TitleMenu)) {
                if (screen instanceof SaturnScreenFabric) {
                    ((SaturnScreenFabric) screen).draw(new ImageTexture(Textures.LOGO_TEXT_BIG).dimensions(180, 18)
                            .position(screen.width - 180 - 20, screen.height - 18 - 20));
                } else {
                    ScreenEvents.afterRender(screen).register((screen1, context, mouseX, mouseY, tickDelta) -> {
                        RenderScope renderScope = new RenderScopeImpl(context.getMatrices(),
                                ((DrawContextAccessor) context).getVertexConsumers());

                        int width = context.getScaledWindowWidth();
                        int height = context.getScaledWindowHeight();

                        float scaleX = (float) screen1.width / width;
                        float scaleY = (float) screen1.height / height;

                        MatrixStack matrixStack = context.getMatrices();
                        matrixStack.push();
                        matrixStack.translate(width - 95, height - 14, 0);
                        matrixStack.scale(0.5f / scaleX, 0.5f / scaleY, 1.0f);
                        renderScope.drawTexture(Textures.LOGO_TEXT_BIG, 0, 0, 0, 0, 180, 18);
                        matrixStack.pop();
                    });
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Only iterate over enabled modules
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
