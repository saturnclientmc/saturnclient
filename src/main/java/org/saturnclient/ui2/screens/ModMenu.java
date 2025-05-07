package org.saturnclient.ui2.screens;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.menus.CloaksMenu;
import org.saturnclient.saturnclient.menus.SaturnConfigEditor;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.SaturnModule;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.elements.Scroll;

public class ModMenu extends SaturnScreen {
    private static ThemeManager theme = new ThemeManager("ModMenu");
    private static Property<Integer> padding = theme.property("padding", new Property<Integer>(25));
    private static Property<Integer> gap = theme.property("gap", new Property<Integer>(10));

    public ModMenu() {
        super("Mod Menu");
    }

    @Override
    public void ui() {
        Scroll scroll = new Scroll(padding.value);

        int row = 0;
        int col = 0;
        
        for (Module mod : ModManager.MODS) {
            scroll.draw(new SaturnModule(mod).position((140 + gap.value) * col, (140 + gap.value) * row));

            if (col == 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }

        int scrollWidth = 420 + (gap.value * 2) + (padding.value * 2);
    
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
