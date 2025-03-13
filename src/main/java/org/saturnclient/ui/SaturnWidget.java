package org.saturnclient.ui;

import net.minecraft.client.gui.DrawContext;

public class SaturnWidget {
    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;
    public float alpha = 1.0f;
    public SaturnAnimation animation = null;
    public boolean visible = true;

    public void render(DrawContext context, boolean hovering) {
    }

    public void click() {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char typedChar) {
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
        if (this.animation == SaturnAnimation.FADE || this.animation == SaturnAnimation.FADE_SLIDE) {
            this.alpha = 0.0f;
        }
        return this;
    }

    public SaturnWidget setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
