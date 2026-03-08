package org.saturnclient.modules.mixins;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import org.saturnclient.feature.interfaces.NametagsInterface.EntityType;
import org.saturnclient.modules.HealthRenderState;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void saturn$captureHealth(T entity, S state, float tickDelta, CallbackInfo ci) {
        if (!(state instanceof HealthRenderState hrs))
            return;

        hrs.saturn$setHealth(entity.getHealth(), entity.getMaxHealth());

        if (entity instanceof PlayerEntity) {
            hrs.saturn$setEntityType(EntityType.PLAYER);
        } else if (entity instanceof HostileEntity) {
            hrs.saturn$setEntityType(EntityType.HOSTILE);
        } else if (entity instanceof PassiveEntity) {
            hrs.saturn$setEntityType(EntityType.PASSIVE);
        } else {
            hrs.saturn$setEntityType(EntityType.OTHER);
        }
    }
}
