package org.saturnclient.ui.components.inputs;

import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.config.Theme;
import org.saturnclient.config.manager.Key;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class KeybindingSelector extends Element {
    Property<Integer> prop;

    public KeybindingSelector(Property<Integer> prop) {
        this.prop = prop;
        this.width = 120;
        this.height = Fonts.getHeight();
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, 0xFF000000);
        String name = MinecraftProvider.PROVIDER.getKeyName(prop.value);

        renderScope.drawText(0.6f,
                prop.value == -1 ? "<NONE>"
                        : name != null ? name.toUpperCase() : "<Invalid-Key>",
                4, 4, Theme.FONT.value, textColor);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Key.GLFW_KEY_BACKSPACE) {
            prop.value = -1;
        } else {
            prop.value = keyCode;
        }
        focused = false;
    }
}
