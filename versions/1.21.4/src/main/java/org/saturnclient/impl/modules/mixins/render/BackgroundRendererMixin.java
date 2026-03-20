package org.saturnclient.impl.modules.mixins.render;

import net.minecraft.client.render.BackgroundRenderer;

import org.saturnclient.feature.features.NoFogFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    // fog
    @ModifyVariable(method = "applyFog", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyFogStart(float viewDistance) {
        if (NoFogFeature.isActive()) {
            return viewDistance * 2.0f;
        }

        return viewDistance;
    }
}