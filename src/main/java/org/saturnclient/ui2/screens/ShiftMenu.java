package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.Button;

public class ShiftMenu extends SaturnScreen {
    public ShiftMenu() {
        super("Shift Menu");
    }

    @Override
    public void ui() {
        draw(new Button("Click Me!", () -> {
            SaturnClient.LOGGER.info("Clicked!");
        }).center(width, height));
    }
}
