package org.saturnclient.saturnmods;

public class ModDimensions {
    public int x = 0;
    public int y = 0;
    public float scale = 1.0f;

    public int width = 0;
    public int height = 0;

    public ModDimensions() {
    }

    public ModDimensions(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ModDimensions(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ModDimensions(int x, int y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public ModDimensions(int x, int y, int width, int height, float scale) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }
}
