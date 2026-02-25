package org.saturnclient.ui2.screens.cosmetics;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideY;
import org.saturnclient.ui2.components.CosmeticPreview;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.components.SkinPreview;
import org.saturnclient.ui2.elements.AnimationStagger;
import org.saturnclient.ui2.elements.Scroll;
import org.saturnclient.ui2.elements.TabMenu;
import org.saturnclient.ui2.elements.TabMenu.TabMenuComponent;

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

        SaturnPlayer player = SaturnPlayer.get();

        AnimationStagger stagger = new AnimationStagger(AnimationConfig.cosmeticsMenu);

        if (player != null) {
            for (String cloak : Cloaks.availableCloaks) {

                stagger.draw(
                        new CosmeticPreview(
                                cloak == player.cloak,
                                Textures.getCloakPreview(cloak),
                                () -> Cloaks.setCloak(cloak))
                                .position((50 + g) * col, (111 + g) * row)
                                .animation(new SlideY(AnimationConfig.cosmeticsMenu, 14)));

                if (col == 5) {
                    col = 0;
                    row++;
                } else {
                    col++;
                }
            }
        }

        scroll.draw(stagger);

        int scrollWidth = 480 + 10 + (g * 2) + (p * 2);

        draw(scroll
                .dimensions(scrollWidth, 350)
                .center(width, height));

        draw(new SkinPreview(-30.0f, false)
                .scale(3.5f)
                .position(scroll.x + (scrollWidth - 220), scroll.y + 40)
                .animation(new Fade(500)));

        draw(new Sidebar(2, this::close)
                .centerOffset(width, height, -(scrollWidth / 2 + 20), 0)
                .animation(new Fade(400)));

        draw(new TabMenu(0,
                new TabMenuComponent(Textures.CLOAK, () -> {
                }),
                new TabMenuComponent(Textures.HAT, () -> {
                    SaturnClient.client.setScreen(new HatMenu());
                }))
                .centerOffset(width, height, 0, -195));
    }
}
