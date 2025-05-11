package org.saturnclient.ui2.components;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
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
            renderScope.matrices.push();
            renderScope.setOpacity(element.opacity);
            renderScope.matrices.translate(element.x, element.y, 0);
            renderScope.matrices.scale(element.scale, element.scale, 1.0f);
            element.render(renderScope, new ElementContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);
        }
    }

    public static void mouseClicked(List<Element> elements, double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Element element : new ArrayList<>(elements)) {
                element.focused = false;

                int adjustedMouseX = (int) mouseX - element.x;
                int adjustedMouseY = (int) mouseY - element.y;

                if (Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width, element.height, element.scale)) {
                    element.focused = true;
                    element.click((int) (adjustedMouseX * element.scale), (int) (adjustedMouseY * element.scale));
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
                    if (Utils.isHovering((int) mouseX - element.x, (int) mouseY - element.y, element.width, element.height, element.scale)) {
                        element.scroll(
                                (int) mouseX - element.x,
                                (int) mouseY - element.y,
                                horizontalAmount,
                                verticalAmount);
                    }
                }
            }
}
