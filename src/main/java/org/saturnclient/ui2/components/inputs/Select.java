package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.resources.Fonts;
import net.minecraft.client.render.RenderLayer;

public class Select extends Element {
    private static ThemeManager theme = new ThemeManager("Toggle");
    private static Property<Integer> fgColor = theme.property("fg-color", Property.color(0xAAAAAA));
    private static Property<Integer> arrowColor = theme.property("arrow-color", Property.color(0xff8B5CF6));
    private static Property<Integer> font = theme.property("font", Property.font(1));

    private Property<Integer> prop;

    public Select(Property<Integer> prop) {
        this.prop = prop;
        this.width = 150;
        this.height = 15;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        renderScope.drawText(0.8f, "<", 0, 0, font.value, arrowColor.value);

        String text = prop.getSelection();

        renderScope.drawText(0.6f, text, (int) (width - ((float) Fonts.getWidth(text, font.value) * 0.6f)) / 2, 1, font.value, fgColor.value);

        renderScope.drawText(0.8f, ">", width - 10, 0, font.value, arrowColor.value);

        theme.setState(null);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (Utils.isHovering(mouseX - (width - 10), mouseY, 10, height, 1.0f)) {
            prop.next();
        } else if (Utils.isHovering(mouseX, mouseY, 10, height, 1.0f)) {
            prop.prev();
        }
    }
}
