package org.saturnclient.ui.elements;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.config.AnimationConfig;
import org.saturnclient.config.Config;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.ElementRenderer;
import org.saturnclient.ui.RenderScope;

public class AnimationStagger extends Element {
    protected List<Element> children = new ArrayList<>();
    public int delay;

    public AnimationStagger(int delay) {
        this.delay = delay;
    }

    public AnimationStagger(AnimationConfig config) {
        this.delay = config.stagger.value;
    }

    public void draw(Element element) {
        if (!Config.stagger.value) {
            element.animation = null;
        }

        if (element.animation != null) {
            element.animation.delay = delay * children.size();
        }

        ElementRenderer.INSTANCE.draw(children, element);

        this.height = Math.max(this.height, element.y + element.height);
        this.width = Math.max(this.width, element.x + element.width);
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        ElementRenderer.INSTANCE.render(children, ctx.elapsed, renderScope,
                ctx.mouseX,
                ctx.mouseY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        ElementRenderer.INSTANCE.mouseClicked(children, mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.INSTANCE.keyPressed(children, keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        ElementRenderer.INSTANCE.mouseDragged(children, mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        ElementRenderer.INSTANCE.mouseReleased(children, mouseX, mouseY, button);
    }
}
