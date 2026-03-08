package org.saturnclient.modules.mixins;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import org.saturnclient.feature.features.FreelookFeature;
import org.saturnclient.modules.CameraOverriddenEntity;
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
        // noinspection ConstantValue — reachable at runtime despite IDE warning
        if (FreelookFeature.isFreeLooking && (Object) this instanceof ClientPlayerEntity) {
            this.cameraPitch = MathHelper.clamp(
                    this.cameraPitch + (float) (yDelta * 0.15),
                    -90.0f, 90.0f);
            this.cameraYaw += (float) (xDelta * 0.15);
            ci.cancel();
        }
    }

    @Override
    @Unique
    public float freelook$getCameraPitch() {
        return cameraPitch;
    }

    @Override
    @Unique
    public float freelook$getCameraYaw() {
        return cameraYaw;
    }

    @Override
    @Unique
    public void freelook$setCameraPitch(float p) {
        cameraPitch = p;
    }

    @Override
    @Unique
    public void freelook$setCameraYaw(float y) {
        cameraYaw = y;
    }
}
