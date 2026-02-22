package org.saturnclient.ui2;

public class ElementContext {
    public int mouseX, mouseY;
    public int elementWidth, elementHeight;
    public float elementScale;
    public long elapsed;

    public ElementContext(long elapsed, int mouseX, int mouseY, Element e) {
        this.mouseX = mouseX - e.x;
        this.mouseY = mouseY - e.y;
        this.elementWidth = e.width;
        this.elementHeight = e.height;
        this.elementScale = e.scale;
        this.elapsed = elapsed;
    }

    public boolean isHovering() {
        return Utils.isHovering(mouseX, mouseY, elementWidth, elementHeight, elementScale);
    }

    public boolean isHovering(int x, int y, int width, int height) {
        return Utils.isHovering(mouseX - x, mouseY - y, width, height, elementScale);
    }
}
