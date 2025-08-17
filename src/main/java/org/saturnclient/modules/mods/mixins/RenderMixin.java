package org.saturnclient.modules.mods.mixins;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.ModManager;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.RenderScope;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(InGameHud.class)
public class RenderMixin {
    @Inject(method = "renderChat", at = @At("TAIL"))
    public void renderMods(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        TextRenderer textRenderer = SaturnClient.client.textRenderer;

        if (textRenderer != null &&
                !(SaturnClient.client.currentScreen instanceof HudEditor)) {
            RenderScope renderScope = new RenderScope(context.getMatrices(),
                    ((DrawContextAccessor) context).getVertexConsumers());

            for (org.saturnclient.modules.Module m : ModManager.MODS) {
                if (m instanceof HudMod && m.isEnabled()) {
                    ModDimensions dim = ((HudMod) m).getDimensions();

                    renderScope.matrices.push();

                    renderScope.matrices.translate(dim.x.value, (float) dim.y.value, 0);

                    renderScope.matrices.scale(dim.scale.value, dim.scale.value, 1.0f);

                    if (dim.renderBackground) {
                        renderScope.drawRoundedRectangle(0, 0, dim.width, dim.height, dim.radius.value,
                                dim.bgColor.value);
                    }

                    renderScope.setRenderLayer(RenderLayer::getGuiTextured);

                    ((HudMod) m).renderHud(renderScope);

                    renderScope.matrices.pop();
                } else {
                    m.render(renderScope);
                }
            }
        }
    }
}
