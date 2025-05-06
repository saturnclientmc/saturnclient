package org.saturnclient.ui2.screens;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.SaturnModule;
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
            scroll.draw(new SaturnModule(mod).position((270 + Scroll.gap.value) * col, (120 + Scroll.gap.value) * row));

            if (col == 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    
        draw(scroll.dimensions(600, 350).center(width, height));
    }
}
