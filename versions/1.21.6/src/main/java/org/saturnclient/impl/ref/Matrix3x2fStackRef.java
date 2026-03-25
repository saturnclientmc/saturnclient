package org.saturnclient.impl.ref;

import java.util.ArrayDeque;
import java.util.Deque;

import org.joml.Matrix3x2fStack;
import org.saturnclient.common.ref.render.MatrixStackRef;

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
        stack.rotate(-t.rotation);
    }

    @Override
    public void scale(float x, float y) {
        stack.scale(x, y);
        if (!customStack.isEmpty()) {
            Transform t = customStack.peek();
            t.scaleX *= x;
            t.scaleY *= y;
        }
    }

    @Override
    public void translate(float x, float y) {
        stack.translate(x, y);
        if (!customStack.isEmpty()) {
            Transform t = customStack.peek();
            t.translateX += x;
            t.translateY += y;
        }
    }

    @Override
    public void rotate(float angle) {
        stack.rotate(angle);
        if (!customStack.isEmpty()) {
            Transform t = customStack.peek();
            t.rotation += angle;
        }
    }

    @Override
    public void rotate(float angle, float originX, float originY, float originZ) {
    }

    private static class Transform {
        float scaleX = 1f, scaleY = 1f;
        float translateX = 0f, translateY = 0f;
        float rotation = 0f;
    }
}