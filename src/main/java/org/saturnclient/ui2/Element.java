package org.saturnclient.ui2;

public class Element {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int color;

    public void render(RenderScope renderScope) {
    }

    public final void dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final void position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
