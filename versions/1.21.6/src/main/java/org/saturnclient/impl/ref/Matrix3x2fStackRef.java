package org.saturnclient.impl.ref;

import org.joml.Matrix3x2fStack;
import org.saturnclient.common.ref.render.MatrixStackRef;

public class Matrix3x2fStackRef implements MatrixStackRef {
    public final Matrix3x2fStack stack;

    public Matrix3x2fStackRef(Matrix3x2fStack stack) {
        this.stack = stack;
    }

    @Override
    public void push() {
        stack.pushMatrix();
    }

    @Override
    public void pop() {
        stack.popMatrix();
    }

    @Override
    public void scale(float x, float y) {
        stack.scale(x, y);
    }

    @Override
    public void translate(float x, float y) {
        stack.translate(x, y);
    }

    @Override
    public void rotate(float angle) {
        stack.rotate(angle);
    }

    @Override
    public void rotate(float angle, float originX, float originY, float originZ) {
        translate(originX, originY);
        rotate(angle);
        translate(-originX, -originY);
    }
}