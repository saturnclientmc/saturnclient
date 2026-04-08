package org.saturnclient.impl.features.mixins.render;

import net.minecraft.client.render.fog.FogRenderer;

import org.saturnclient.mod.mods.NoFogMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    // fog
    @ModifyVariable(method = "applyFog", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyFogStart(float viewDistance) {
        if (NoFogMod.isActive()) {
            return viewDistance * 2.0f;
        }

        return viewDistance;
    }
}