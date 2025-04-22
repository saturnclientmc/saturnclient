package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

import org.saturnclient.saturnclient.mod_utils.CameraOverriddenEntity;
import org.saturnclient.saturnmods.mods.FreeLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    boolean firstTime = true;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 1, shift = At.Shift.AFTER))
    public void lockRotation(BlockView focusedBlock, Entity cameraEntity, boolean isThirdPerson, boolean isFrontFacing,
            float tickDelta, CallbackInfo ci) {
        if (FreeLook.isFreeLooking && cameraEntity instanceof ClientPlayerEntity) {
            CameraOverriddenEntity cameraOverriddenEntity = (CameraOverriddenEntity) cameraEntity;

            if (firstTime && SaturnClient.client.player != null) {
                cameraOverriddenEntity.freelook$setCameraPitch(SaturnClient.client.player.getPitch());
                cameraOverriddenEntity.freelook$setCameraYaw(SaturnClient.client.player.getYaw());
                firstTime = false;
            }
            this.setRotation(cameraOverriddenEntity.freelook$getCameraYaw(),
                    cameraOverriddenEntity.freelook$getCameraPitch());

        }
        if (!FreeLook.isFreeLooking && cameraEntity instanceof ClientPlayerEntity) {
            firstTime = true;
        }
    }

}
