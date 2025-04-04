package org.saturnclient.ui;

import net.minecraft.client.gui.DrawContext;

public class SaturnWidget {

    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;
    public float alpha = 1.0f;
    public float scale = 1.0f;
    public SaturnAnimation[] animations = null;
    public boolean visible = true;
    public boolean focused = false;

    public void init() {
    }

    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
    }

    public void click(int mouseX, int mouseY) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char typedChar) {
    }

    public void tick() {
    }

    public void mouseScrolled(
            int mouseX,
            int mouseY,
            double horizontalAmount,
            double verticalAmount) {
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

    public SaturnWidget setAnimations(SaturnAnimation... animations) {
        this.animations = animations;
        for (SaturnAnimation anim : this.animations) {
            anim.init(this);
        }
        return this;
    }

    public SaturnWidget setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public SaturnWidget setScale(float scale) {
        this.scale = scale;
        return this;
    }
}
