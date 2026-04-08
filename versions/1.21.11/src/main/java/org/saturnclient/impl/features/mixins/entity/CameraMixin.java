package org.saturnclient.impl.features.mixins.entity;

import org.saturnclient.impl.features.entity.CameraOverriddenEntity;
import org.saturnclient.mod.mods.FreelookMod;
import org.saturnclient.saturnclient.SaturnClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    boolean firstTime = true;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 1, shift = At.Shift.AFTER))
    public void lockRotation(
            World focusedBlock,
            Entity cameraEntity,
            boolean isThirdPerson,
            boolean isFrontFacing,
            float tickDelta,
            CallbackInfo ci) {
        if (FreelookMod.isFreeLooking() && cameraEntity instanceof ClientPlayerEntity) {
            CameraOverriddenEntity cameraOverriddenEntity = (CameraOverriddenEntity) cameraEntity;

            if (firstTime && SaturnClient.client.player != null) {
                cameraOverriddenEntity.freelook$setCameraPitch(SaturnClient.client.player.getPitch());
                cameraOverriddenEntity.freelook$setCameraYaw(SaturnClient.client.player.getYaw());
                firstTime = false;
            }
            this.setRotation(cameraOverriddenEntity.freelook$getCameraYaw(),
                    cameraOverriddenEntity.freelook$getCameraPitch());

        }
        if (!FreelookMod.isFreeLooking() && cameraEntity instanceof ClientPlayerEntity) {
            firstTime = true;
        }
    }

}