package org.saturnclient.ui2.screens;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.SaturnModule;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.Scroll;
import org.saturnclient.ui2.resources.Textures;

public class ModMenu extends SaturnScreen {
    public ModMenu() {
        super("Mod Menu");
    }

    @Override
    public void ui() {
        int p = 10;
        int g = 10;

        Scroll scroll = new Scroll(p);

        int row = 0;
        int col = 0;

        for (Module mod : ModManager.ALL_MODS) {
            scroll.draw(new SaturnModule(mod).position((160 + g) * col, (50 + g) * row));

            if (col == 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }

        int scrollWidth = 480 + 10 + (g * 2) + (p * 2);

        draw(scroll.dimensions(scrollWidth, 350).center(width, height));

        draw(new Sidebar(0, this::close).centerOffset(width, height, -(scrollWidth / 2 + 20), 0));

        draw(new ImageTexture(Textures.LOGO_TEXT_BIG).dimensions(180, 18).position(width - 180 - 20, height - 18 - 20));
    }
}
