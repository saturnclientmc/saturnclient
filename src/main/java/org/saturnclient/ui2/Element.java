package org.saturnclient.ui2;

import org.saturnclient.ui2.anim.Animation;

public class Element {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean focused;
    public Animation animation;

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
        this.x = (w - width) / 2;
        this.y = (h - height) / 2;

        return this;
    }

    public final Element centerOffset(int w, int h, int offsetX, int offsetY) {
        this.x = (w - width) / 2 + offsetX;
        this.y = (h - height) / 2 + offsetY;

        return this;
    }

    public final Element animation(Animation animation) {
        this.animation = animation;

        return this;
    }
}
