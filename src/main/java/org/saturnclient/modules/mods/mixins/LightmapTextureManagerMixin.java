// code modified from Gamma utils: https://github.com/Sjouwer/gamma-utils
package org.saturnclient.modules.mods.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.LightmapTextureManager;
import org.saturnclient.modules.mods.Fullbright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    
    @ModifyExpressionValue(
        method = "update",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 0)
    )
    private float modifyGamma(float original) {
        if (Fullbright.shouldOverrideBrightness()) {
            return Fullbright.getBrightnessValue();
        }
        return original;
    }
}
