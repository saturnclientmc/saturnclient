package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.resources.Fonts;

public class IntInput extends Input {
    public Property<Integer> prop;

    public IntInput(Property<Integer> prop) {
        this.width = 120;
        this.height = Fonts.getHeight();
        this.prop = prop;
        this.text = String.valueOf(prop.value);
    }

    @Override
    public void charTyped(char chr) {
        if ((Character.isDigit(chr) || chr == '.') && text.length() < 10) {
            if (text.equals("0") && cursorPosition == 1) {
                text = "" + chr;
            } else {
                text = text.substring(0, cursorPosition) +
                        chr +
                        text.substring(cursorPosition);

                prop.value = text.isEmpty() ? 0 : Integer.parseInt(text);
                ConfigManager.save();

                cursorPosition++;
            }
        }
    }

    @Override
    public void backspace() {
        if (text.length() == 0) {
            text = "0";
            cursorPosition = 1;
        }
        prop.value = Integer.parseInt(text);
        ConfigManager.save();
    }

    @Override
    public void checkReset() {
        if (prop.isReset) {
            this.text = String.valueOf(prop.value);
            this.cursorPosition = 0;
            prop.isReset = false;
        }
    }
}
