package org.saturnclient.ui.components.inputs;

import org.saturnclient.config.Theme;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;

public class Toggle extends Element {
    private Property<Boolean> prop;

    public Toggle(Property<Boolean> prop) {
        this.prop = prop;
        this.width = 60;
        this.height = 30;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, height,
                Theme.withAlpha(0.5f, prop.value ? Theme.ACCENT.value : Theme.PRIMARY.value));
        renderScope.drawRoundedRectangle(prop.value ? 30 : 0, 0, 30, 30, 30,
                prop.value ? Theme.ACCENT.value : Theme.PRIMARY.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        prop.value = !prop.value;
    }
}
