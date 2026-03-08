package org.saturnclient.impl.modules.mixins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

import org.saturnclient.feature.Feature;
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

/**
 * Drives per-frame rendering for all registered features.
 *
 * This mixin is intentionally kept thin: it creates a {@link RenderScope},
 * iterates the enabled feature list from {@link FeatureManager}, and
 * dispatches either {@link HudFeature#renderHud} (for HUD elements) or
 * {@link Feature#render} (for world-overlay features like Crosshair).
 *
 * No feature-specific logic lives here — all decisions about what to
 * draw are made inside each feature class.
 *
 * Changes from the original:
 * - Package imports updated to the refactored feature names (no
 * more {@code feature.features.*}).
 * - The HUD-editor screen check still uses the existing
 * {@link SaturnScreenFabric} / {@link HudEditor} pair; that is
 * unchanged infrastructure.
 */
@Mixin(InGameHud.class)
public class RenderMixin {

    @Inject(method = "renderChat", at = @At("TAIL"))
    public void renderMods(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        TextRenderer textRenderer = SaturnClient.client.textRenderer;

        // Skip rendering while the HUD editor is open so the editor
        // can draw its own preview layout without double-drawing.
        if (textRenderer == null)
            return;
        if (SaturnClient.client.currentScreen instanceof SaturnScreenFabric f
                && f.screen instanceof HudEditor)
            return;

        RenderScope scope = new RenderScopeImpl(
                context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        for (Feature feature : FeatureManager.ENABLED_MODS) {
            if (!feature.isEnabled())
                continue;

            if (feature instanceof HudFeature hud) {
                FeatureLayout dim = hud.getDimensions();

                scope.getMatrixStack().push();
                scope.getMatrixStack().translate(dim.x.value, (float) dim.y.value, 0f);
                scope.getMatrixStack().scale(dim.scale.value, dim.scale.value, 1.0f);

                if (dim.renderBackground) {
                    scope.drawRoundedRectangle(0, 0, dim.width, dim.height,
                            dim.radius.value, dim.bgColor.value);
                }

                hud.renderHud(scope);
                scope.getMatrixStack().pop();

            } else {
                // Non-HUD features (e.g. CrosshairFeature) use the plain render hook.
                feature.render(scope);
            }
        }
    }
}
