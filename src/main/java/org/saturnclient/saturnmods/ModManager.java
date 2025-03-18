package org.saturnclient.saturnmods;

import org.saturnclient.saturnclient.SaturnClient;
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
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

            if (textRenderer != null) {
                SaturnClient.textRenderer = textRenderer;

                for (SaturnMod m : MODS) {
                    if (m instanceof HudMod && m.isEnabled()) {
                        ModDimensions dim = ((HudMod) m).getDimensions();

                        MatrixStack matrices = context.getMatrices();
                        matrices.push();

                        matrices.translate(dim.x, dim.y, 0);

                        matrices.scale(dim.scale, dim.scale, 1.0f);

                        ((HudMod) m).render(context);

                        matrices.pop();
                    }
                }
            }
        });
    }
}
