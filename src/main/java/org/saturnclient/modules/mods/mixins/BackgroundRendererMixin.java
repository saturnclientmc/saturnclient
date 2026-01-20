package org.saturnclient.modules.mods.mixins;

import net.minecraft.client.render.BackgroundRenderer;

import org.saturnclient.modules.mods.NoFog;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    
    @ModifyVariable(method = "applyFog", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyFogStart(float viewDistance) {
        if (NoFog.isActive()) {
            return Float.MAX_VALUE; // this could be customised
        }
        return viewDistance;
    }
    // liquids
    @Redirect(
        method = "applyFog",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSpectator()Z")
    )
    private static boolean redirectIsSpectator(Entity entity) {
        if (NoFog.isActive() && NoFog.liquids()) {
            return true; // pretend to be spectator
        }
        return entity.isSpectator();
    }
}
