package org.saturnclient.ui2;

import java.util.function.Function;

import org.saturnclient.ui2.anim.Animation;
import org.saturnclient.ui2.anim.Curve;

public class Element {
    public int x;
    public int y;
    public int width;
    public int height;
    public float opacity = 1.0f;
    public boolean focused;
    public Animation animation;
    public float scale = 1.0f;
    public Function<Double, Double> curve = Curve::easeInOutCubic;

    public void render(RenderScope renderScope, RenderContext ctx) {
    }

    public void click(int mouseX, int mouseY) {
    }

    public final Element dimensions(int width, int height) {
        this.width = width;
        this.height = height;

        return this;
    }

    public final Element position(int x, int y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public final Element center(int w, int h) {
        this.centerOffset(w, h, 0, 0);
        return this;
    }

    public final Element centerOffset(int w, int h, int offsetX, int offsetY) {
        this.x = (w - (int) (width * scale)) / 2 + offsetX;
        this.y = (h - (int) (height * scale)) / 2 + offsetY;

        return this;
    }

    public final Element animation(Animation animation) {
        this.animation = animation;
        return this;
    }

    public final Element animation(Animation animation, Function<Double, Double> curve) {
        this.animation = animation;
        this.curve = curve;
        return this;
    }

    public Element scale(float scale) {
        this.scale = scale;
        return this;
    }
}
