package org.saturnclient.ui2.screens;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.ConfigEditor;
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

        int row = 0;
        int col = 0;
        
        for (Module mod : ModManager.MODS) {
            scroll.draw(new Button(mod.getName(), () -> {
                SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
            }).dimensions(100, 50).position((100 + Scroll.gap.value) * col, (50 + Scroll.gap.value) * row));

            if (col == 3) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    
        draw(scroll.dimensions(400, 250).center(width, height));
    }
}
