package org.saturnclient.impl.ref;

import java.util.ArrayDeque;
import java.util.Deque;

import org.joml.Matrix3x2fStack;
import org.saturnclient.common.ref.render.MatrixStackRef;
import org.saturnclient.common.ref.render.QuaternionfRef;

public class Matrix3x2fStackRef implements MatrixStackRef {
    public final Matrix3x2fStack stack;

    // Our custom stack stores the reverse transforms
    private final Deque<Transform> customStack = new ArrayDeque<>();

    public Matrix3x2fStackRef(Matrix3x2fStack stack) {
        this.stack = stack;
    }

    @Override
    public void push() {
        // Push a new empty Transform
        customStack.push(new Transform());
    }

    @Override
    public void pop() {
        if (customStack.isEmpty())
            return;

        // Apply inverse transforms in reverse order
        Transform t = customStack.pop();
        if (t.scaleX != 1 || t.scaleY != 1)
            stack.scale(1f / t.scaleX, 1f / t.scaleY);
        stack.translate(-t.translateX, -t.translateY);
        // rotation inversion can be added here if needed
    }

    @Override
    public void scale(float x, float y, float z) {
        stack.scale(x, y);
        if (!customStack.isEmpty()) {
            Transform t = customStack.peek();
            t.scaleX *= x;
            t.scaleY *= y;
        }
    }

    @Override
    public void translate(float x, float y, float z) {
        stack.translate(x, y);
        if (!customStack.isEmpty()) {
            Transform t = customStack.peek();
            t.translateX += x;
            t.translateY += y;
        }
    }

    @Override
    public void translate(double x, double y, double z) {
        translate((float) x, (float) y, (float) z);
    }

    @Override
    public void multiply(QuaternionfRef quaternion) {
        // Not implemented yet
    }

    @Override
    public void multiply(QuaternionfRef quaternion, float originX, float originY, float originZ) {
        // Not implemented yet
    }

    @Override
    public void loadIdentity() {
        customStack.clear();
    }

    @Override
    public Object peek() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return customStack.isEmpty();
    }

    private static class Transform {
        float scaleX = 1f, scaleY = 1f;
        float translateX = 0f, translateY = 0f;
        // rotation can be added if needed
    }
}