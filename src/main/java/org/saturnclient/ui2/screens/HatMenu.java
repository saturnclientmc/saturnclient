package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.menus.SaturnConfigEditor;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.CosmeticPreview;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.components.SkinPreview;
import org.saturnclient.ui2.elements.Scroll;

public class HatMenu extends SaturnScreen {
    public HatMenu() {
        super("Hats Menu");
    }

    @Override
    public void ui() {
        int p = 10;
        int g = 10;

        Scroll scroll = new Scroll(p);

        int row = 0;
        int col = 0;
        
        SaturnPlayer player = Auth.players.get(Auth.uuid);

        if (player != null) {
            for (String hat : Hats.availableHats) {
                scroll.draw(new CosmeticPreview(hat == player.hat, Textures.getHatPreview(hat), () -> {
                    Hats.setHat(Auth.uuid, hat);
                }).dimensions(50, 50).position((50 + g) * col, (50 + g) * row));
    
                if (col == 5) {
                    col = 0;
                    row++;
                } else {
                    col++;
                }
            }
        }

        int scrollWidth = 480 + 10 + (g * 2) + (p * 2);
    
        draw(scroll.dimensions(scrollWidth, 350).center(width, height));

        draw(new SkinPreview(170f, true).scale(3.5f).position(scrollWidth - 40 - p, 200));

        draw(new Sidebar(
            3,
            new Sidebar.SidebarComponent(Textures.MODS_TAB, () -> {
                SaturnClient.client.setScreen(new ModMenu());
            }, false),
            new Sidebar.SidebarComponent(Textures.SETTINGS, () -> {
                SaturnClient.client.setScreen(new SaturnConfigEditor());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOAK, () -> {
                SaturnClient.client.setScreen(new CloakMenu());
            }, false),

            new Sidebar.SidebarComponent(Textures.HAT, () -> {
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOSE, () -> {
                this.close();
            }, true)
        ).centerOffset(width, height, -(scrollWidth / 2 + 20), 0));
    }
}
