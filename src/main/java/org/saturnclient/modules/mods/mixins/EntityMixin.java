package org.saturnclient.modules.mods.mixins;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import org.saturnclient.modules.mods.Freelook;
import org.saturnclient.modules.mods.utils.CameraOverriddenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements CameraOverriddenEntity {
	@Unique
	private float cameraPitch;

	@Unique
	private float cameraYaw;

	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	public void changeCameraLookDirection(double xDelta, double yDelta, CallbackInfo ci) {
        //noinspection ConstantValue// IntelliJ is incorrect here, this code block is reachable
        if (Freelook.isFreeLooking && (Object) this instanceof ClientPlayerEntity) {
            double pitchDelta = (yDelta * 0.15);
            double yawDelta = (xDelta * 0.15);

            this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float) pitchDelta, -90.0f, 90.0f);
            this.cameraYaw += (float) yawDelta;

            ci.cancel();

        }
    }

	@Override
	@Unique
	public float freelook$getCameraPitch() {
		return this.cameraPitch;
	}

	@Override
	@Unique
	public float freelook$getCameraYaw() {
		return this.cameraYaw;
	}

	@Override
	@Unique
	public void freelook$setCameraPitch(float pitch) {
		this.cameraPitch = pitch;
	}

	@Override
	@Unique
	public void freelook$setCameraYaw(float yaw) {
		this.cameraYaw = yaw;
	}
}