package org.saturnclient.ui;

import net.minecraft.client.gui.DrawContext;

public class SaturnWidget {
    public int x;
    public int y;
    public int width;
    public int height;
    public float alpha = 1.0f;
    public SaturnAnimation animation = null;

    public void render(DrawContext context, boolean hovering) {
    }

    public void click() {
    }

    public SaturnWidget setX(int x) {
        this.x = x;
        return this;
    }

    public SaturnWidget setY(int y) {
        this.y = y;
        return this;
    }

    public SaturnWidget setWidth(int width) {
        this.width = width;
        return this;
    }

    public SaturnWidget setHeight(int height) {
        this.height = height;
        return this;
    }

    public SaturnWidget setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public SaturnWidget setAnimation(SaturnAnimation animation) {
        this.animation = animation;
        switch (animation) {
            case FADE:
                this.alpha = 0.0f;
                break;
            case SLIDE:
                this.animation.widgetY = y;
                y += animation.distance;
                break;
            case FADE_SLIDE:
                this.animation.widgetY = y;
                y += animation.distance;
                this.alpha = 0.0f;
                break;
        }
        return this;
    }
}
