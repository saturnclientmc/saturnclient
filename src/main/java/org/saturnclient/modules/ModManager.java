package org.saturnclient.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;

import org.saturnclient.modules.mods.*;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.RenderScope;

public class ModManager {
    public static Module[] MODS = {
        new Crosshair(),
        new AutoSprint(),
        new ArmorDisplay(),
        new Fps(),
        new PotionDisplay()
    };

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Module m : MODS) {
                if (m.isEnabled()) {
                    m.tick();
                }
            }
        });

        HudRenderCallback.EVENT.register((context, _o) -> {
            TextRenderer textRenderer = SaturnClient.client.textRenderer;

            if (textRenderer != null &&
                    !(SaturnClient.client.currentScreen instanceof HudEditor)) {
                RenderScope renderScope = new RenderScope(context.getMatrices(),
                    ((DrawContextAccessor) context).getVertexConsumers());

                for (Module m : MODS) {
                    if (m instanceof HudMod && m.isEnabled()) {
                        ModDimensions dim = ((HudMod) m).getDimensions();

                        renderScope.matrices.push();

                        renderScope.matrices.translate(dim.x.value, (float) dim.y.value, 0);

                        renderScope.matrices.scale(dim.scale.value, dim.scale.value, 1.0f);

                        renderScope.drawRoundedRectangle(0, 0, dim.width, dim.height, dim.radius.value, dim.bgColor.value);

                        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

                        ((HudMod) m).renderHud(renderScope);

                        renderScope.matrices.pop();
                    } else {
                        m.render(renderScope);
                    }
                }
            }
        });
    }
}
