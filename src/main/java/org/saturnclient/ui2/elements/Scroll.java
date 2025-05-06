package org.saturnclient.ui2.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.components.ElementRenderer;

public class Scroll extends Element {
    protected List<Element> children = new ArrayList<>();
    int scroll = 0;
    int maxScroll = 0;

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.matrices.push();
        renderScope.matrices.translate(0, -scroll, 0);
        ElementRenderer.render(children, renderScope, ctx.mouseX, ctx.mouseY);
        renderScope.matrices.pop();
        // renderScope.drawRoundedRectangle(width - 10, 0, 10, 50, 0, Button.fgColor.value);
    }

    @Override
    public void scroll(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
        if (scroll - verticalAmount <= 0) {
            scroll = 0;
        } else if (scroll - verticalAmount <= maxScroll) {
            scroll -= verticalAmount;
        }
    }

    public void draw(Element element) {
        children.add(element);

        if (element.width > width) {
            width = element.width;
        }

        if (element.height > height) {
            height = element.height;
        }

        maxScroll = Math.max(maxScroll, element.y - height);
    }
}
