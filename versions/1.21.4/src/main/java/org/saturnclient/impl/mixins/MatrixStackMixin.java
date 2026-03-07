package org.saturnclient.impl.mixins;

import org.joml.Quaternionf;
import org.saturnclient.common.bindings.SaturnQuaternionf;
import org.saturnclient.common.render.IMatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.util.math.MatrixStack;

@Mixin(MatrixStack.class)
public abstract class MatrixStackMixin implements IMatrixStack {

    @Shadow
    public abstract void translate(double x, double y, double z);

    @Shadow
    public abstract void translate(float x, float y, float z);

    @Shadow
    public abstract void scale(float x, float y, float z);

    @Shadow
    public abstract void multiply(Quaternionf quaternion);

    @Shadow
    public abstract void multiply(Quaternionf quaternion, float originX, float originY, float originZ);

    @Shadow
    public abstract void push();

    @Shadow
    public abstract void pop();

    @Invoker("peek")
    public abstract MatrixStack.Entry peekM();

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract void loadIdentity();

    @Override
    public void multiply(SaturnQuaternionf quaternion) {
        this.multiply((Quaternionf) quaternion.inner);
    }

    @Override
    public void multiply(SaturnQuaternionf quaternion, float originX, float originY, float originZ) {
        this.multiply((Quaternionf) quaternion.inner, originX, originY, originZ);
    }

    @Override
    public Object peek() {
        return this.peekM();
    }
}