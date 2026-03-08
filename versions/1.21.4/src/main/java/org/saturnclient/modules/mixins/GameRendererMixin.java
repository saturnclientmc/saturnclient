package org.saturnclient.modules.mixins;

import net.minecraft.client.render.GameRenderer;

import org.saturnclient.feature.features.ZoomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(CallbackInfoReturnable<Float> cir) {
        if (ZoomFeature.shouldZoom()) {
            cir.setReturnValue(cir.getReturnValue() / ZoomFeature.getZoomLevel());
        }
    }
}
