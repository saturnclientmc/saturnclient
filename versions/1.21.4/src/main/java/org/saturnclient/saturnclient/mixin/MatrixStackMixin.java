package org.saturnclient.saturnclient.mixin;

import org.joml.Quaternionf;
import org.saturnclient.common.minecraft.bindings.SaturnQuaternionf;
import org.saturnclient.common.minecraft.render.IMatrixStack;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.util.math.MatrixStack;

@Mixin(MatrixStack.class)
public class MatrixStackMixin extends MatrixStack implements IMatrixStack {
    @Override
    public void multiply(SaturnQuaternionf quaternion) {
        this.multiply((Quaternionf) quaternion.inner);
    }

    @Override
    public void multiply(SaturnQuaternionf quaternion, float originX, float originY, float originZ) {
        this.multiply((Quaternionf) quaternion.inner, originX, originY, originZ);
    }
}
