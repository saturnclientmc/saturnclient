package org.saturnclient.ui2.components;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.anim.Animation;

public class ElementRenderer {
    public static void draw(List<Element> elements, Element element) {
        synchronized (elements) {
            elements.add(element);
        }
        
        if (element.animation != null) {
            element.animation.init(element);

            Animation.execute((Float progress) -> {
                element.animation.tick(progress, element);
            }, element.animation.duration);
        }
    }

    public static void render(List<Element> elements, RenderScope renderScope, int mouseX, int mouseY) {
        for (Element element : elements) {
            renderScope.enableScissor(element.x, element.y, element.x + element.width, element.y + element.height);

            renderScope.matrices.push();
            renderScope.setOpacity(element.opacity);
            renderScope.matrices.translate(element.x, element.y, 0);
            renderScope.matrices.scale(element.scale, element.scale, 1.0f);
            element.render(renderScope, new RenderContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);

            renderScope.disableScissor();
        }
    }

    public static void mouseClicked(List<Element> elements, double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;
        if (button == 0) {
            for (Element element : new ArrayList<>(elements)) {
                element.focused = false;

                int adjustedMouseX = (int) mouseX - element.x;
                int adjustedMouseY = (int) mouseY - element.y;

                if (Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width, element.height, element.scale)) {
                    element.focused = true;
                    element.click(adjustedMouseX, adjustedMouseY);
                }
            }
        }
    }

    public static void mouseScrolled(
            List<Element> elements,
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
                for (Element element : new ArrayList<>(elements)) {
                    double adjustedMouseX = mouseX - element.x;
                    double adjustedMouseY = mouseY - element.y;
                    boolean isMouseInside = adjustedMouseX >= 0 &&
                            adjustedMouseX <= (element.width * element.scale) &&
                            adjustedMouseY >= 0 &&
                            adjustedMouseY <= (element.height * element.scale);
        
                    if (isMouseInside) {
                        element.scroll(
                                (int) (adjustedMouseX / element.scale),
                                (int) (adjustedMouseY / element.scale),
                                horizontalAmount,
                                verticalAmount);
                    }
                }
            }
}
