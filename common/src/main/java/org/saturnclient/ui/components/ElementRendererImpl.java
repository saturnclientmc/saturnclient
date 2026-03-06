package org.saturnclient.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.ElementRenderer;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.Utils;

public class ElementRendererImpl extends ElementRenderer {
    public void draw(List<Element> elements, Element element) {
        synchronized (elements) {
            elements.add(element);
        }

        if (element.animation != null) {
            element.animation.init(element);
        }
    }

    public void render(List<Element> elements, long elapsed, RenderScope renderScope, int mouseX, int mouseY) {
        for (Element element : elements) {
            element.playAnimationFrame(elapsed);

            renderScope.getMatrixStack().push();
            renderScope.setOpacity(element.opacity);
            renderScope.getMatrixStack().translate(element.x, element.y, 0);
            renderScope.getMatrixStack().scale(element.scale, element.scale, 1.0f);
            element.render(renderScope, new ElementContext(elapsed, mouseX, mouseY, element));
            renderScope.getMatrixStack().pop();
        }
    }

    public void mouseClicked(List<Element> elements, double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Element element : new ArrayList<>(elements)) {
                element.focused = false;

                int adjustedMouseX = (int) mouseX - element.x;
                int adjustedMouseY = (int) mouseY - element.y;

                element.mouseClicked(adjustedMouseX * element.scale, adjustedMouseY * element.scale, button);

                if (element.opacity > 0 && Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width,
                        element.height, element.scale)) {
                    element.focused = true;
                    element.click((int) (adjustedMouseX * element.scale), (int) (adjustedMouseY * element.scale));
                }
            }
        }
    }

    public void mouseScrolled(
            List<Element> elements,
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        for (Element element : new ArrayList<>(elements)) {
            if (Utils.isHovering((int) mouseX - element.x, (int) mouseY - element.y, element.width, element.height,
                    element.scale)) {
                element.scroll(
                        (int) mouseX - element.x,
                        (int) mouseY - element.y,
                        horizontalAmount,
                        verticalAmount);
            }
        }
    }

    public void keyPressed(List<Element> elements, int keyCode, int scanCode, int modifiers) {
        for (Element element : new ArrayList<>(elements)) {
            if (element.focused && element.opacity > 0) {
                element.keyPressed(keyCode, scanCode, modifiers);

                char typedChar = Utils.getCharFromKey(keyCode, modifiers);
                if (typedChar != '\0') {
                    element.charTyped(typedChar);
                }
            }
        }
    }

    public void mouseDragged(List<Element> elements, double mouseX, double mouseY, int button, double deltaX,
            double deltaY) {
        for (Element element : new ArrayList<>(elements)) {
            element.mouseDragged(mouseX - element.x, mouseY - element.y, button, deltaX, deltaY);
        }
    }

    public void mouseReleased(List<Element> elements, double mouseX, double mouseY, int button) {
        for (Element element : new ArrayList<>(elements)) {
            element.mouseReleased(mouseX - element.x, mouseY - element.y, button);
        }
    }
}
