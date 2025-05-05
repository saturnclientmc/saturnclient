package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.Button;
import org.saturnclient.ui2.elements.Scroll;

public class ModMenu extends SaturnScreen {
    public ModMenu() {
        super("Mod Menu");
    }

    @Override
    public void ui() {
        Scroll scroll = new Scroll();

        scroll.draw(new Button("Hello World!", () -> {
            SaturnClient.LOGGER.info("Clicked Hello World!");
        }));

        draw(scroll);
    }
}
