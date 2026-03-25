package org.saturnclient.impl.mixins;

import org.joml.Quaternionfc;
import org.saturnclient.common.ref.render.MatrixStackRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Mixin(MatrixStack.class)
public abstract class MatrixStackMixin implements MatrixStackRef {
    @Shadow
    public abstract void push();

    @Shadow
    public abstract void pop();

    @Shadow
    public abstract void translate(float x, float y, float z);

    @Shadow
    public abstract void scale(float x, float y, float z);

    @Shadow
    public abstract void multiply(Quaternionfc quaternion);

    @Shadow
    public abstract void multiply(Quaternionfc quaternion, float originX, float originY, float originZ);

    @Override
    public void translate(float x, float y) {
        translate(x, y, 0);
    }

    @Override
    public void scale(float x, float y) {
        scale(x, y, 1.0f);
    }

    @Override
    public void rotate(float angle, float originX, float originY, float originZ) {
        multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(angle), originX, originY, originZ);
    }

    @Override
    public void rotate(float angle) {
        multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(angle));
    }
}