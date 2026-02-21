package org.saturnclient.ui2.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.components.ElementRenderer;

public class Scroll extends Element {
    private static Property<Integer> scrollBarRadius = Property.integer(10);
    private static Property<Integer> scrollBarWidth = Property.integer(5);
    private static Property<Integer> scrollBarPadding = Property.integer(5);

    int padding = 0;

    protected List<Element> children = new ArrayList<>();
    int scroll = 0;
    int maxScroll = 0;

    public Scroll(int padding) {
        this.padding = padding;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        calculateMaxScroll();

        renderScope.drawRoundedRectangle(0, 0, width, height,
                Theme.BG_RADIUS.value, Theme.BACKGROUND.value);

        renderScope.enableScissor(padding, padding, width - padding, height - padding);
        renderScope.matrices.push();
        renderScope.matrices.translate(padding, -scroll + padding, 0);

        ElementRenderer.render(children, ctx.elapsed, renderScope,
                ctx.mouseX - padding,
                ctx.mouseY - padding + scroll);

        renderScope.matrices.pop();
        renderScope.disableScissor();

        if (maxScroll > 0) {
            renderScope.drawRoundedRectangle(
                    width - scrollBarWidth.value - scrollBarPadding.value,
                    calculateScrollBarY(),
                    scrollBarWidth.value,
                    calculateScrollBarHeight(),
                    scrollBarRadius.value,
                    Theme.SCROLL.value);
        }
    }

    @Override
    public void scroll(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
        scroll -= verticalAmount;
        if (scroll < 0) {
            scroll = 0;
        } else if (scroll > maxScroll) {
            scroll = maxScroll;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        ElementRenderer.mouseClicked(children, mouseX - padding, mouseY - padding + scroll, 0);
    }

    public void draw(Element element) {
        children.add(element);
    }

    @Override
    public Element dimensions(int width, int height) {
        maxScroll = 0;

        for (Element element : children) {
            maxScroll = Math.max(maxScroll, element.y + element.height - height);
        }

        maxScroll = Math.max(0, maxScroll); // never negative
        return super.dimensions(width, height);
    }

    int calculateScrollBarHeight() {
        if (maxScroll <= 0)
            return height - (scrollBarPadding.value * 2);
        return Math.max(20, (height * height) / (height + maxScroll)) - (scrollBarPadding.value * 2);
    }

    int calculateScrollBarY() {
        int scrollBarHeight = calculateScrollBarHeight();
        if (maxScroll <= 0)
            return scrollBarPadding.value;
        return (scroll * (height - scrollBarHeight)) / maxScroll + scrollBarPadding.value;
    }

    private void calculateMaxScroll() {
        maxScroll = 0;

        for (Element element : children) {
            int bottom = element.y + element.height;
            maxScroll = Math.max(maxScroll, bottom - height + padding);
        }

        if (maxScroll > 0) {
            maxScroll = maxScroll + 10;
        } else {
            maxScroll = 0;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.keyPressed(children, keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        ElementRenderer.mouseDragged(children, mouseX - padding, mouseY - padding + scroll, button, deltaX, deltaY);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        ElementRenderer.mouseReleased(children, mouseX - padding, mouseY - padding + scroll, button);
    }
}
