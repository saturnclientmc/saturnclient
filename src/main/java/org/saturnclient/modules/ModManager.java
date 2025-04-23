package org.saturnclient.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import org.saturnclient.modules.mods.*;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.menus.HudEditor;

public class ModManager {
    public static Module[] MODS = {
        new Crosshair()
    };

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            for (Module m : MODS) {
                if (m instanceof TickModule && m.isEnabled()) {
                    ((TickModule) m).tick();
                }
            }
        });

        HudRenderCallback.EVENT.register((context, _o) -> {
            TextRenderer textRenderer = SaturnClient.client.textRenderer;

            if (textRenderer != null &&
                    !(SaturnClient.client.currentScreen instanceof HudEditor)) {
                SaturnClientConfig.textRenderer = textRenderer;

                for (Module m : MODS) {
                    if (m instanceof HudMod && m.isEnabled()) {
                        ModDimensions dim = ((HudMod) m).getDimensions();

                        MatrixStack matrices = context.getMatrices();
                        matrices.push();

                        matrices.translate(dim.x.value, (float) dim.y.value, 0);

                        matrices.scale(dim.scale.value, dim.scale.value, 1.0f);

                        ((HudMod) m).render(context);

                        matrices.pop();
                    }
                }
            }
        });
    }
}
