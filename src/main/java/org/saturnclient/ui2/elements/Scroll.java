package org.saturnclient.ui2.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.components.ElementRenderer;

public class Scroll extends Element implements ElementRenderer {
    protected List<Element> children = new ArrayList<>();

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
    }

    @Override
    public void draw(Element element) {
        children.add(element);

        if (element.width > width) {
            width = element.width;
        }

        if (element.height > height) {
            height = element.height;
        }
    }

    @Override
    public void scroll(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
        for (Element child : children) {
            child.y -= verticalAmount;
        }
    }
    

    @Override
    public List<Element> getChildren() {
        return children;
    }
}
