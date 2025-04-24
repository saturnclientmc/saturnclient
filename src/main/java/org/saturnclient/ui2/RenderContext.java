package org.saturnclient.ui2;

public class RenderContext {
    public int mouseX, mouseY;
    public int elementX, elementY;
    public int elementWidth, elementHeight;

    public RenderContext(int mouseX, int mouseY, Element e) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.elementX = e.x;
        this.elementY = e.y;
        this.elementWidth = e.width;
        this.elementHeight = e.height;
    }

    public boolean isHovering() {
        return mouseX >= elementX &&
               mouseX <= elementX + elementWidth &&
               mouseY >= elementY &&
               mouseY <= elementY + elementHeight;
    }
}
