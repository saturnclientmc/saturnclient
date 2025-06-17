package org.saturnclient.ui2.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.components.ElementRenderer;

public class Scroll extends Element {
    private static ThemeManager theme = new ThemeManager("Scroll");
    private static Property<Integer> bgColor = theme.property("bg-color", Property.color(0x90000000));
    private static Property<Integer> scrollBarColor = theme.property("scrollbar-color", Property.color(-7643914));

    private static Property<Integer> scrollBarRadius = theme.property("scrollbar-radius", Property.integer(10));
    private static Property<Integer> scrollBarWidth = theme.property("scrollbar-width", Property.integer(5));
    private static Property<Integer> scrollBarPadding = theme.property("scrollbar-padding", Property.integer(5));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", Property.integer(10));
    
    int padding = 0;

    protected List<Element> children = new ArrayList<>();
    int scroll = 0;
    int maxScroll = 0;

    public Scroll(int padding) {
        this.padding = padding;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        renderScope.enableScissor(padding, padding, width - padding, height - padding);
        renderScope.matrices.push();
        renderScope.matrices.translate(padding, (-scroll) + padding, 0);
        ElementRenderer.render(children, renderScope, ctx.mouseX - padding, ctx.mouseY - padding + scroll);
        renderScope.matrices.pop();
        renderScope.disableScissor();

        renderScope.drawRoundedRectangle(width - scrollBarWidth.value - scrollBarPadding.value, calculateScrollBarY(), scrollBarWidth.value, calculateScrollBarHeight(), scrollBarRadius.value, scrollBarColor.value);
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

        if (element.width > width) {
            width = element.width;
        }

        if (element.height > height) {
            height = element.height;
        }

        maxScroll = Math.max(maxScroll, element.y - maxScroll);
    }

    int calculateScrollBarHeight() {
        if (maxScroll <= 0) return height - (scrollBarPadding.value * 2); // No scrolling needed
        return Math.max(20, (height * height) / (height + maxScroll)) - (scrollBarPadding.value * 2); // At least 20px tall
    }

    int calculateScrollBarY() {
        int scrollBarHeight = calculateScrollBarHeight();
        if (maxScroll <= 0) return scrollBarPadding.value;
        return (scroll * (height - scrollBarHeight)) / maxScroll + scrollBarPadding.value;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.keyPressed(children, keyCode, scanCode, modifiers);
    }
}
