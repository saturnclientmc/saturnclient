package org.saturnclient.impl.ref;

import org.joml.Matrix3x2fStack;
import org.saturnclient.common.ref.render.MatrixStackRef;
import org.saturnclient.common.ref.render.QuaternionfRef;

public class Matrix3x2fStackRef implements MatrixStackRef {
    private final Matrix3x2fStack stack;

    public Matrix3x2fStackRef(Matrix3x2fStack stack) {
        this.stack = stack;
    }

    @Override
    public void scale(float x, float y, float z) {
        stack.scale(x, y);
    }

    @Override
    public void multiply(QuaternionfRef quaternion) {
    }

    @Override
    public void multiply(QuaternionfRef quaternion, float originX, float originY, float originZ) {
    }

    @Override
    public Object peek() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void loadIdentity() {
    }

    @Override
    public void pop() {
        stack.popMatrix();
    }

    @Override
    public void push() {
        stack.pushMatrix();
    }

    @Override
    public void translate(float x, float y, float z) {
        stack.translate(x, y);
    }

    @Override
    public void translate(double x, double y, double z) {
        stack.translate((float) x, (float) y);
    }
}