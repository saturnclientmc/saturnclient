package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.Button;

public class SaturnMenu extends SaturnScreen {
    public SaturnMenu() {
        super("Saturn Client");
    }

    @Override
    public void ui() {
        draw(new Button("Settings", false).center(width, height));
    }
}
