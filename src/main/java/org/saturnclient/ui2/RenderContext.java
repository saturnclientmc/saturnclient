package org.saturnclient.ui2;

public class RenderContext {
    public int mouseX, mouseY;
    public int elementX, elementY;
    public int elementWidth, elementHeight;
    public float elementScale;

    public RenderContext(int mouseX, int mouseY, Element e) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.elementX = e.x;
        this.elementY = e.y;
        this.elementWidth = e.width;
        this.elementHeight = e.height;
        this.elementScale = e.scale;
    }

    public boolean isHovering() {
        return Utils.isHovering(mouseX - elementX, mouseY - elementY, elementWidth, elementHeight, elementScale);
    }
}
