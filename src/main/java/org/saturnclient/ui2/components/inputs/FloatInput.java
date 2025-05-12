package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.Element;

public class FloatInput extends Element {
    public FloatInput() {
        this.width = 120;
        this.height = 120;
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void charTyped(char typedChar) {
        SaturnClient.LOGGER.info("Char typed: "+typedChar);
    }
}
