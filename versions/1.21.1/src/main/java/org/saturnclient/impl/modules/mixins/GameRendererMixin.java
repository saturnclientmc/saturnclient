package org.saturnclient.impl.modules.mixins;

import net.minecraft.client.render.GameRenderer;

import org.saturnclient.feature.features.ZoomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides the game's field-of-view when {@link ZoomFeature} is active.
 *
 * The old code checked both {@code Zoom.isZooming} and
 * {@code Zoom.shouldZoom()} separately. The refactored
 * {@link ZoomFeature#shouldZoom()} already ORs both conditions
 * ({@code enabled.value && isZooming}), so only one check is needed.
 *
 * No {@link org.saturnclient.common.module.ModuleProvider} is required
 * here because zooming is a pure render-time override with no per-tick
 * engine queries.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * After the vanilla FOV has been computed, divide it by the zoom
     * level to produce the narrowed field-of-view.
     */
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(CallbackInfoReturnable<Float> cir) {
        if (ZoomFeature.shouldZoom()) {
            float zoomedFov = cir.getReturnValue() / ZoomFeature.getZoomLevel();
            cir.setReturnValue(zoomedFov);
        }
    }
}
