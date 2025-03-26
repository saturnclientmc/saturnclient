package org.saturnclient.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.saturnclient.saturnclient.SaturnClient;
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
        verticalAmount *= 2;

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
        context.enableScissor(0, 0, width, height);

        for (SaturnWidget child : children) {
            if (!child.visible) continue;

            boolean isMouseInside =
                child.x < mouseX &&
                child.x + child.width > mouseX &&
                child.y < mouseY &&
                child.y + child.height > mouseY;

            if (child.y - scroll < height && child.y + child.height > scroll) {
                MatrixStack matrices = context.getMatrices();
                matrices.push();
                matrices.translate(child.x, child.y - scroll, 0);
                matrices.scale(child.scale, child.scale, 1.0f);

                child.render(
                    context,
                    isMouseInside,
                    mouseX - child.x,
                    mouseY - (child.y - scroll)
                );
                matrices.pop();
            }
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
        for (SaturnWidget child : children) {
            if (!child.visible || !child.focused) {
                continue;
            }

            child.keyPressed(keyCode, scanCode, modifiers);
            break;
        }
    }

    @Override
    public void charTyped(char typedChar) {
        for (SaturnWidget child : children) {
            if (!child.visible || !child.focused) {
                continue;
            }

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
                continue;
            }

            double adjustedMouseX = (mouseX - widget.x) / widget.scale;
            double adjustedMouseY = (mouseY - widget.y) / widget.scale;
            boolean isMouseInside =
                adjustedMouseX >= 0 &&
                adjustedMouseX <= widget.width &&
                adjustedMouseY >= 0 &&
                adjustedMouseY <= widget.height;

            if (isMouseInside) {
                widget.focused = true;
                widget.click((int) adjustedMouseX, (int) adjustedMouseY);
            }
        }
    }
}
