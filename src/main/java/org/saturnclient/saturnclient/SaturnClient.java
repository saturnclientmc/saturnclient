package org.saturnclient.saturnclient;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import org.saturnclient.modules.ModManager;
import org.saturnclient.saturnclient.client.ServiceClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.saturnclient.cosmetics.Emotes;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.saturnclient.event.KeyInputHandler;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.resources.Textures;
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
        SaturnClientConfig.init();
        ModManager.init();

        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> ConfigManager.save());

        SpecialModelLoaderEvents.LOAD_SCOPE.register(() -> {
            return (resourceManager, location) -> MOD_ID.equals(location.getNamespace());
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen != null && !(screen instanceof TitleMenu)) {
                if (screen instanceof SaturnScreen) {
                    ((SaturnScreen) screen).draw(new ImageTexture(Textures.LOGO_TEXT_BIG).dimensions(180, 18)
                            .position(screen.width - 180 - 20, screen.height - 18 - 20));
                } else {
                    ScreenEvents.afterRender(screen).register((screen1, context, mouseX, mouseY, tickDelta) -> {
                        RenderScope renderScope = new RenderScope(context.getMatrices(),
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
