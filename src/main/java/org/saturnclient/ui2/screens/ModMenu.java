package org.saturnclient.ui2.screens;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.CloaksMenu;
import org.saturnclient.saturnclient.menus.SaturnConfigEditor;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.SaturnModule;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.elements.Scroll;

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
        
        for (Module mod : ModManager.MODS) {
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

        draw(new Sidebar(
            0,
            new Sidebar.SidebarComponent(Textures.MODS_TAB, () -> {
            }, false),
            new Sidebar.SidebarComponent(Textures.SETTINGS, () -> {
                SaturnClient.client.setScreen(new SaturnConfigEditor());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOAK, () -> {
                SaturnClient.client.setScreen(new CloaksMenu());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOSE, () -> {
                this.close();
            }, true)
        ).centerOffset(width, height, -(scrollWidth / 2 + 20), 0));
    }
}
