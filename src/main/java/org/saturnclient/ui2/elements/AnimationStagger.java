package org.saturnclient.ui2.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.components.ElementRenderer;

public class AnimationStagger extends Element {
    protected List<Element> children = new ArrayList<>();
    public int delay;

    public AnimationStagger(int delay) {
        this.delay = delay;
    }

    public void draw(Element element) {
        if (!SaturnClientConfig.stagger.value) {
            element.animation = null;
        }

        if (element.animation != null) {
            element.animation.delay = delay * children.size();
        }

        ElementRenderer.draw(children, element);
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        ElementRenderer.render(children, ctx.elapsed, renderScope,
                ctx.mouseX,
                ctx.mouseY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        ElementRenderer.mouseClicked(children, mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.keyPressed(children, keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        ElementRenderer.mouseDragged(children, mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        ElementRenderer.mouseReleased(children, mouseX, mouseY, button);
    }
}
