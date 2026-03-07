package org.saturnclient.ui;

import java.util.List;

public abstract class ElementRenderer {
    public static ElementRenderer INSTANCE;

    public abstract void draw(List<Element> elements, Element element);

    public abstract void render(List<Element> elements, long elapsed, RenderScope renderScope, int mouseX, int mouseY);

    public abstract void mouseClicked(List<Element> elements, double mouseX, double mouseY, int button);

    public abstract void mouseScrolled(
            List<Element> elements,
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount);

    public abstract void keyPressed(List<Element> elements, int keyCode, int scanCode, int modifiers);

    public abstract void mouseDragged(List<Element> elements, double mouseX, double mouseY, int button, double deltaX,
            double deltaY);

    public abstract void mouseReleased(List<Element> elements, double mouseX, double mouseY, int button);
}
