package org.saturnclient.modules.mixins;

import org.saturnclient.feature.FeatureLayout;
import org.saturnclient.feature.FeatureManager;
import org.saturnclient.feature.HudFeature;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.RenderScopeImpl;
import org.saturnclient.ui.SaturnScreenFabric;
import org.saturnclient.ui.screens.HudEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(InGameHud.class)
public class RenderMixin {
    @Inject(method = "renderChat", at = @At("TAIL"))
    public void renderMods(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        TextRenderer textRenderer = SaturnClient.client.textRenderer;

        if (textRenderer != null &&
                !(SaturnClient.client.currentScreen instanceof SaturnScreenFabric f && f.screen instanceof HudEditor)) {
            RenderScope renderScope = new RenderScopeImpl(context.getMatrices(),
                    ((DrawContextAccessor) context).getVertexConsumers());

            for (org.saturnclient.feature.Feature m : FeatureManager.ENABLED_MODS) {
                if (m instanceof HudFeature && m.isEnabled()) {
                    FeatureLayout dim = ((HudFeature) m).getDimensions();

                    renderScope.getMatrixStack().push();

                    renderScope.getMatrixStack().translate(dim.x.value, (float) dim.y.value, 0);

                    renderScope.getMatrixStack().scale(dim.scale.value, dim.scale.value, 1.0f);

                    if (dim.renderBackground) {
                        renderScope.drawRoundedRectangle(0, 0, dim.width, dim.height, dim.radius.value,
                                dim.bgColor.value);
                    }

                    ((HudFeature) m).renderHud(renderScope);

                    renderScope.getMatrixStack().pop();
                } else {
                    m.render(renderScope);
                }
            }
        }
    }
}
