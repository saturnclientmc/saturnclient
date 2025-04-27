package org.saturnclient.ui2;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SaturnScreen extends Screen {
    protected List<Element> elements = new ArrayList<>();

    public SaturnScreen(String title) {
        super(Text.literal(title));
        ThemeManager.load();
    }

    public void draw(Element element) {
        synchronized (elements) {
            elements.add(element);
        }
    }

    @Override
    protected void init() {
        ui(); // abstraction to render the saturn ui and also render extra stuff here
    }

    public void ui() {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // We are using a Abstracted RenderScope because older minecraft versions don't
        // use DrawContext
        RenderScope renderScope = new RenderScope(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        renderScope.matrices.push();

        for (Element element : elements) {
            renderScope.matrices.push();
            renderScope.matrices.translate(element.x, element.y, 0);
            element.render(renderScope, new RenderContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);
        }

        renderScope.matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (Element element : new ArrayList<>(elements)) {
                element.focused = false;

                int adjustedMouseX = (int) mouseX - element.x;
                int adjustedMouseY = (int) mouseY - element.y;

                if (Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width, element.height)) {
                    element.focused = true;
                    element.click(adjustedMouseX, adjustedMouseY);
                }
            }
        }

        return false;
    }
}
