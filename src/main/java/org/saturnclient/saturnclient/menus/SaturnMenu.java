package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.Text;

public class SaturnMenu extends SaturnScreen {
    public SaturnMenu() {
        super("Saturn Client");
    }

    @Override
    public void ui() {
        draw(new Text("Saturn Client"));
    }
}
