package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.render.MatrixStackRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.util.math.MatrixStack;

@Mixin(MatrixStack.class)
public abstract class MatrixStackMixin implements MatrixStackRef {
    @Shadow
    public abstract void translate(float x, float y, float z);

    @Shadow
    public abstract void scale(float x, float y, float z);

    @Shadow
    public abstract void push();

    @Shadow
    public abstract void pop();

    @Override
    public void translate(float x, float y) {
        translate(x, y, 0);
    }

    @Override
    public void scale(float x, float y) {
        scale(x, y, 1.0f);
    }
}