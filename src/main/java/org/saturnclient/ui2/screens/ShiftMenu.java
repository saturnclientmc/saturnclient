package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.ModMenu;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.Button;

public class ShiftMenu extends SaturnScreen {
    public ShiftMenu() {
        super("Shift Menu");
    }

    @Override
    public void ui() {
        draw(new Button("Settings", () -> {
            SaturnClient.client.setScreen(new ModMenu());
        }).center(width, height));
    }
}
