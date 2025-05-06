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

        scroll.draw(new Button("Hello World! 1", () -> {
            SaturnClient.LOGGER.info("Clicked Hello World!");
        }).position(0, 0));
        
        scroll.draw(new Button("Hello World! 1", () -> {
            SaturnClient.LOGGER.info("Clicked Hello World!");
        }).position(0, 50));

        scroll.draw(new Button("Hello World! 2", () -> {
            SaturnClient.LOGGER.info("Clicked Hello World!");
        }).position(0, 100));

        scroll.draw(new Button("Hello World! 3", () -> {
            SaturnClient.LOGGER.info("Clicked Hello World!");
        }).position(0, 150));

        draw(scroll.dimensions(width, 100));
    }
}
