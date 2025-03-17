package org.saturnclient.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.ui.SaturnWidget;

import net.minecraft.client.gui.DrawContext;

public class SaturnScroll extends SaturnWidget {
    private int scroll = 0;
    private int maxScroll = 0;
    private List<SaturnWidget> children = new ArrayList<>();

    @Override
    public void mouseScrolled(int mouseX, int mouseY, double horizontalAmount,
            double verticalAmount) {
        if (scroll - verticalAmount <= 0) {
            scroll = 0;
        } else if (scroll - verticalAmount >= maxScroll) {
            scroll = maxScroll;
        } else {
            scroll -= verticalAmount;
        }
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.enableScissor(x, y, x + width, y + height);

        for (SaturnWidget child : children) {
            int xx = child.x;
            int yy = child.y;

            child.x += x;
            child.y += y - scroll;

            boolean isMouseInside = child.x < mouseX && child.x + child.width > mouseX
                    && child.y < mouseY && child.y + child.height > mouseY;

            if (child.y < y + height && child.y + child.height > y) {
                child.render(context, isMouseInside, mouseX, mouseY);
            }

            child.x = xx;
            child.y = yy;
        }

        context.disableScissor();
    }

    @Override
    public void click(int mouseX, int mouseY) {
        mouseX = mouseX - x;
        mouseY = mouseY - y;
        for (SaturnWidget child : children) {
            boolean isMouseInside = child.x < mouseX && child.x + child.width > mouseX
                    && child.y < mouseY && child.y + child.height > mouseY;

            if (isMouseInside) {
                child.click(mouseX, mouseY);
                return;
            }
        }
    }

    @Override
    public void init() {
        if (isScrollable()) {
            for (SaturnWidget child : children) {
                maxScroll = Math.max(maxScroll, child.y - height);
            }
        }
    }

    public void draw(SaturnWidget widget) {
        synchronized (children) {
            widget.init();
            children.add(widget);
        }
    }

    public boolean isScrollable() {
        for (SaturnWidget child : children) {
            if (child.y + child.height > height) {
                return true;
            }
        }
        return false;
    }
}
