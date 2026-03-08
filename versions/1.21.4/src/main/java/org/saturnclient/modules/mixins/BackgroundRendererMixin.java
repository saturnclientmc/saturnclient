package org.saturnclient.modules.mixins;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;

import org.saturnclient.feature.features.NoFogFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @ModifyVariable(method = "applyFog", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyFogStart(float viewDistance) {
        if (NoFogFeature.isActive()) {
            return Float.MAX_VALUE;
        }
        return viewDistance;
    }

    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSpectator()Z"))
    private static boolean redirectIsSpectator(Entity entity) {
        if (NoFogFeature.isActive() && NoFogFeature.liquids()) {
            return true; // pretend to be spectator to bypass liquid fog
        }
        return entity.isSpectator();
    }
}
