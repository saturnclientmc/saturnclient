package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.resources.Fonts;

public class FloatInput extends Input {
    public Property<Float> prop;

    public FloatInput(Property<Float> prop) {
        this.width = 120;
        this.height = Fonts.getHeight();
        this.prop = prop;
        this.text = String.valueOf(prop.value);
    }

    @Override
    public void charTyped(char chr) {
        if ((Character.isDigit(chr) || chr == '.' || (chr == '-' && cursorPosition == 0)) && text.length() < 10) {
            if (text.equals("0") && cursorPosition == 1) {
                text = "" + chr;
            } else {
                text = text.substring(0, cursorPosition) +
                        chr +
                        text.substring(cursorPosition);

                try {
                    prop.value = text.isEmpty() ? 0 : Float.parseFloat(text);
                    cursorPosition++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void backspace() {
        try {
            if (text.length() == 0) {
                text = "0";
                cursorPosition = 1;
            }
            prop.value = Float.parseFloat(text);
            ConfigManager.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
