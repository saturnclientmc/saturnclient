package org.saturnclient.modules.mods.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.saturnclient.modules.mods.Zoom;

import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(CallbackInfoReturnable<Float> cir) {
        if (Zoom.isZooming && Zoom.shouldZoom()) {
            float originalFov = cir.getReturnValue();
            float zoomedFov = originalFov / Zoom.getZoomLevel();
            cir.setReturnValue(zoomedFov);
        }
    }
}
