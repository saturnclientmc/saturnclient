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
        ui();

        // Do stuff
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

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);

            renderScope.matrices.push();
            renderScope.matrices.translate(element.x, element.y, 0);
            element.render(renderScope, isMouseInside(mouseX, mouseY, element));
            renderScope.matrices.pop();
        }

        renderScope.matrices.pop();
    }

    public static boolean isMouseInside(int mouseX, int mouseY, Element element) {
        return mouseX >= element.x &&
               mouseX <= (element.x + element.width) &&
               mouseY >= element.y &&
               mouseY <= (element.y + element.height);
    }    
}
