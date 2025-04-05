package org.saturnclient.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
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

        for (SaturnWidget widget : children) {
            if (!widget.visible) continue;

            double adjustedMouseX = (mouseX - widget.x);
            double adjustedMouseY = (mouseY - widget.y + scroll);
            boolean isMouseInside =
                adjustedMouseX >= 0 &&
                adjustedMouseX <= (widget.width * widget.scale) &&
                adjustedMouseY >= 0 &&
                adjustedMouseY <= (widget.height * widget.scale);

            if (
                widget.y - scroll < height && widget.y + widget.height > scroll
            ) {
                MatrixStack matrices = context.getMatrices();
                matrices.push();
                matrices.translate(widget.x, widget.y - scroll, 0);
                matrices.scale(widget.scale, widget.scale, 1.0f);

                widget.render(
                    context,
                    isMouseInside,
                    (int) (adjustedMouseX / widget.scale),
                    (int) (adjustedMouseY / widget.scale)
                );
                matrices.pop();
            }
        }
        context.disableScissor();
    }

    @Override
    public void init() {
        maxScroll = 0;
        for (SaturnWidget child : children) {
            maxScroll = Math.max(maxScroll, child.y + child.height - height);
        }
    }

    public void draw(SaturnWidget widget) {
        synchronized (children) {
            widget.init();
            children.add(widget);
        }
    }

    public boolean isScrollable() {
        for (SaturnWidget widget : children) {
            if (widget.y + widget.height > height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SaturnWidget widget : children) {
            if (!widget.visible || !widget.focused) {
                continue;
            }

            widget.keyPressed(keyCode, scanCode, modifiers);
            break;
        }
    }

    @Override
    public void charTyped(char typedChar) {
        for (SaturnWidget widget : children) {
            if (!widget.visible || !widget.focused) {
                continue;
            }

            widget.charTyped(typedChar);
            break;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        for (SaturnWidget widget : children) {
            widget.focused = false;
            if (!widget.visible) {
                continue;
            }

            double adjustedMouseX = (mouseX - widget.x);
            double adjustedMouseY = (mouseY - (widget.y - scroll));
            boolean isMouseInside =
                adjustedMouseX >= 0 &&
                adjustedMouseX <= (widget.width * widget.scale) &&
                adjustedMouseY >= 0 &&
                adjustedMouseY <= (widget.height * widget.scale);

            if (isMouseInside) {
                widget.focused = true;
                widget.click(
                    (int) (adjustedMouseX / widget.scale),
                    (int) (adjustedMouseY / widget.scale)
                );
            }
        }
    }

    public void clear() {
        children.clear();
    }
}
