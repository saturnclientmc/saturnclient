package org.saturnclient.ui;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class SaturnScreen {
    public static interface ScreenProvider {
        public void close();

        public int getWidth();

        public int getHeight();
    }

    // General
    public int width;
    public int height;
    public String title;

    // Saturn Screen
    public List<Element> elements = new ArrayList<>();
    public float backgroundOpacity = 1.0f;
    protected Instant start = null;
    public int backgroundBlur = 10;

    // Provider
    public ScreenProvider provider;

    public SaturnScreen(String title) {
        this.title = title;
    }

    protected void init() {
        elements.clear();
        width = provider.getWidth();
        height = provider.getHeight();
        ui(); // abstraction to render the saturn ui and also render extra stuff here
    }

    public abstract void ui();

    public void draw(Element element) {
        ElementRenderer.INSTANCE.draw(elements, element);
    }

    // --------------
    // Rendering
    // --------------

    public void render(RenderScope renderScope, int mouseX, int mouseY, float delta, long elapsed) {
        renderScope.getMatrixStack().push();

        renderScope.getMatrixStack().scale(0.5f, 0.5f, 0.5f);

        ElementRenderer.INSTANCE.render(new ArrayList<>(elements), elapsed, renderScope, mouseX, mouseY);

        renderScope.getMatrixStack().pop();
    }

    // --------------
    // Events
    // --------------

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.INSTANCE.mouseClicked(elements, mouseX, mouseY, button);

        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.INSTANCE.mouseDragged(elements, mouseX, mouseY, button, deltaX, deltaY);

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.INSTANCE.mouseReleased(elements, mouseX, mouseY, button);

        return false;
    }

    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.INSTANCE.mouseScrolled(elements, mouseX, mouseY, horizontalAmount, verticalAmount);

        return false;
    }

    public void resize(int width, int height) {
        elements.clear();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.INSTANCE.keyPressed(elements, keyCode, scanCode, modifiers);

        return false;
    }

    public boolean shouldPause() {
        return true;
    }

    public void onClose() {
    }
}
