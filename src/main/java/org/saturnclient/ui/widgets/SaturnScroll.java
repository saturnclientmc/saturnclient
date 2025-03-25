package org.saturnclient.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import org.saturnclient.ui.SaturnWidget;

public class SaturnScroll extends SaturnWidget {

    private int scroll = 0;
    private int maxScroll = 0;
    private List<SaturnWidget> children = new ArrayList<>();

    @Override
    public void mouseScrolled(
        int mouseX,
        int mouseY,
        double horizontalAmount,
        double verticalAmount
    ) {
        if (scroll - verticalAmount <= 0) {
            scroll = 0;
        } else if (scroll - verticalAmount >= maxScroll) {
            scroll = maxScroll;
        } else {
            scroll -= verticalAmount;
        }
    }

    @Override
    public void render(
        DrawContext context,
        boolean hovering,
        int mouseX,
        int mouseY
    ) {
        context.enableScissor(x, y, x + width, y + height);

        for (SaturnWidget child : children) {
            if (!child.visible) continue;

            int xx = child.x;
            int yy = child.y;

            child.x += x;
            child.y += y - scroll;

            boolean isMouseInside =
                child.x < mouseX &&
                child.x + child.width > mouseX &&
                child.y < mouseY &&
                child.y + child.height > mouseY;

            if (child.y < y + height && child.y + child.height > y) {
                child.render(context, isMouseInside, mouseX, mouseY);
            }

            child.x = xx;
            child.y = yy;
        }

        context.disableScissor();
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

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        System.out.println(
            "[SaturnScroll] Key Pressed: keyCode=" +
            keyCode +
            ", scanCode=" +
            scanCode +
            ", modifiers=" +
            modifiers
        );

        for (SaturnWidget child : children) {
            if (!child.visible || !child.focused) {
                System.out.println(
                    "[SaturnScroll] Skipping child " +
                    child +
                    " (Not visible or focused)"
                );
                continue;
            }

            System.out.println(
                "[SaturnScroll] Passing key event to child: " + child
            );
            child.keyPressed(keyCode, scanCode, modifiers);
            break;
        }
    }

    @Override
    public void charTyped(char typedChar) {
        System.out.println("[SaturnScroll] Char Typed: '" + typedChar + "'");

        for (SaturnWidget child : children) {
            if (!child.visible || !child.focused) {
                System.out.println(
                    "[SaturnScroll] Skipping child " +
                    child +
                    " (Not visible or focused)"
                );
                continue;
            }

            System.out.println(
                "[SaturnScroll] Passing char event to child: " + child
            );
            child.charTyped(typedChar);
            break;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        System.out.println(
            "[SaturnScroll] Click at: x=" + mouseX + ", y=" + mouseY
        );

        mouseX -= x;
        mouseY -= (y - scroll);

        for (SaturnWidget widget : children) {
            widget.focused = false;
            if (!widget.visible) {
                System.out.println(
                    "[SaturnScroll] Skipping hidden child: " + widget
                );
                continue;
            }

            boolean isMouseInside =
                widget.x < mouseX &&
                widget.x + widget.width > mouseX &&
                widget.y < mouseY &&
                widget.y + widget.height > mouseY;

            if (isMouseInside) {
                System.out.println("[SaturnScroll] Child clicked: " + widget);
                widget.focused = true;
                widget.click(mouseX, mouseY);
            }
        }
    }
}
