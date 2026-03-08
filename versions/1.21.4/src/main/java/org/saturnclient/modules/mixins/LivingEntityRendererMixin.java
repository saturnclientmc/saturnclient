package org.saturnclient.modules.mixins;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import org.saturnclient.common.module.EntityModule;
import org.saturnclient.modules.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Captures health and entity-type into the render state each frame so
 * that the nametag replacement logic never needs to touch the live entity.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void saturn$captureHealth(T entity, S state, float tickDelta, CallbackInfo ci) {
        if (!(state instanceof HealthRenderState hrs))
            return;

        hrs.saturn$setHealth(entity.getHealth(), entity.getMaxHealth());

        EntityModule.EntityType type;
        if (entity instanceof PlayerEntity)
            type = EntityModule.EntityType.PLAYER;
        else if (entity instanceof HostileEntity)
            type = EntityModule.EntityType.HOSTILE;
        else if (entity instanceof PassiveEntity)
            type = EntityModule.EntityType.PASSIVE;
        else
            type = EntityModule.EntityType.OTHER;

        hrs.saturn$setEntityType(type);
    }
}
