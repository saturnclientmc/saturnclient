package org.saturnclient.impl.features.mixins.render;

// Code approach adapted from Gamma Utils: https://github.com/Sjouwer/gamma-utils

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.LightmapTextureManager;

import org.saturnclient.mod.mods.FullbrightMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Overrides the lightmap's gamma computation when
 * {@link FullbrightMod} is active, replacing the vanilla
 * brightness with a user-configured value.
 *
 * The old import was {@code org.saturnclient.Mod.Mods.Fullbright};
 * it is now {@link FullbrightMod}. The injection target and
 * strategy are unchanged.
 */
@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

    /**
     * If fullbright is active, substitute the Mod's brightness
     * value for whatever vanilla calculated.
     */
    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 0))
    private float modifyGamma(float original) {
        if (FullbrightMod.shouldOverrideBrightness()) {
            return FullbrightMod.getBrightnessValue();
        }
        return original;
    }
}
