package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.components.CosmeticPreview;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.components.SkinPreview;
import org.saturnclient.ui2.elements.Scroll;

public class CloakMenu extends SaturnScreen {
    public CloakMenu() {
        super("Cloaks Menu");
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
            for (String cloak : Cloaks.availableCloaks) {
                scroll.draw(new CosmeticPreview(cloak == player.cloak, Textures.getCloakPreview(cloak), () -> {
                    Cloaks.setCloak(Auth.uuid, cloak);
                }).position((50 + g) * col, (111 + g) * row));
    
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

        draw(new SkinPreview(-30.0f, false).scale(3.5f).position(scrollWidth - 40 - p, 200));

        draw(new Sidebar(2, this::close).centerOffset(width, height, -(scrollWidth / 2 + 20), 0));
    }
}
