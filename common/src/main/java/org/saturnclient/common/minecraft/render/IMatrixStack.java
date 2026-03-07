package org.saturnclient.common.minecraft.render;

import org.saturnclient.common.minecraft.bindings.SaturnQuaternionf;

public interface IMatrixStack {
    public void translate(double x, double y, double z);

    public void translate(float x, float y, float z);

    public void scale(float x, float y, float z);

    public void multiply(SaturnQuaternionf quaternion);

    public void multiply(SaturnQuaternionf quaternion, float originX, float originY, float originZ);

    public void push();

    public void pop();

    public Object peek();

    public boolean isEmpty();

    public void loadIdentity();
}
