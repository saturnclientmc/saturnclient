package org.saturnclient.saturnmods;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.saturnmods.mods.ArmorDisplay;
import org.saturnclient.saturnmods.mods.Coordinates;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class ModManager {
    public static SaturnMod[] MODS = {
            new Coordinates(),
            new ArmorDisplay()
    };

    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            if (textRenderer != null && !(client.currentScreen instanceof HudEditor)) {
                SaturnClient.textRenderer = textRenderer;

                for (SaturnMod m : MODS) {
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
