package org.saturnclient.modules.mods.mixins;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.saturnclient.modules.mods.utils.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void saturn$captureHealth(T entity, S state, float tickDelta, CallbackInfo ci) {
        if (state instanceof HealthRenderState hrs) {
            hrs.saturn$setHealth(entity.getHealth(), entity.getMaxHealth());
        }
    }
}
