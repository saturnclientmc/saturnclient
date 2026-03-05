package org.saturnclient.ui2;

import org.saturnclient.ui2.anim.Animation;

public class Element {
    public int x;
    public int y;
    public int width;
    public int height;
    public float opacity = 1.0f;
    public boolean focused;
    public Animation animation;
    public float scale = 1.0f;
    public Integer duration = null; // Element duration, will fade out if not null

    public void render(RenderScope renderScope, ElementContext ctx) {
    }

    public void click(int mouseX, int mouseY) {
    }

    public void scroll(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char typedChar) {
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public void playAnimationFrame(long elapsed) {
        if (this.animation != null && elapsed >= this.animation.delay) {
            this.animation.tick(this.animation.curve
                    .apply(Math.min(1.0, (double) (elapsed - this.animation.delay) / this.animation.duration)), this);
        }
    }

    public Element dimensions(int width, int height) {
        this.width = width;
        this.height = height;

        return this;
    }

    public Element position(int x, int y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public Element centerHorizontal(int w, int h, int offsetX, int offsetY) {
        this.x = (w - (int) (width * scale)) / 2 + offsetX;
        this.y = offsetY;

        return this;
    }

    public Element center(int w, int h) {
        this.centerOffset(w, h, 0, 0);
        return this;
    }

    public Element centerOffset(int w, int h, int offsetX, int offsetY) {
        this.x = (w - (int) (width * scale)) / 2 + offsetX;
        this.y = (h - (int) (height * scale)) / 2 + offsetY;

        return this;
    }

    public Element animation(Animation animation) {
        this.animation = animation;
        return this;
    }

    public Element scale(float scale) {
        this.scale = scale;
        return this;
    }
}
