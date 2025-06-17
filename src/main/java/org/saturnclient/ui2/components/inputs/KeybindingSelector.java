package org.saturnclient.ui2.components.inputs;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class KeybindingSelector extends Element {
    private static ThemeManager theme = new ThemeManager("Input");
    private static Property<Integer> font = theme.property("font", Property.font(1));
    Property<Integer> prop;

    public KeybindingSelector(Property<Integer> prop) {
        this.prop = prop;
        this.width = 120;
        this.height = Fonts.getHeight();
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        int scanCode = GLFW.glfwGetKeyScancode(prop.value);

        renderScope.drawRoundedRectangle(0, 0, width, height, 10, 0xFF000000);
        renderScope.drawText(0.6f, GLFW.glfwGetKeyName(prop.value, scanCode).toUpperCase(), 4, 4, font.value, textColor);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        prop.value = keyCode;
        focused = false;
    }
}
