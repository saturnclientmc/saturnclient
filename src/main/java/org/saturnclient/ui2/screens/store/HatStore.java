package org.saturnclient.ui2.screens.store;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.components.CosmeticPreview;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.Notification.NotificationKind;
import org.saturnclient.ui2.elements.Scroll;
import org.saturnclient.ui2.elements.TabMenu;
import org.saturnclient.ui2.elements.TabMenu.TabMenuComponent;
import org.saturnclient.ui2.elements.Text;
import org.saturnclient.ui2.resources.Fonts;
import org.saturnclient.ui2.resources.Textures;

public class HatStore extends SaturnScreen {
    private long lastPurchaseTime = 0;

    public HatStore() {
        super("Hat Store");
    }

    public HatStore(long lastPurchaseTime) {
        super("Hat Store");
        this.lastPurchaseTime = lastPurchaseTime;
    }

    @Override
    public void ui() {
        int p = 10;
        int gx = 10;
        int gy = 20;

        Scroll scroll = new Scroll(p);

        int row = 0;
        int col = 0;

        SaturnPlayer player = Auth.players.get(Auth.uuid);

        if (player != null) {
            for (String hat : Hats.ALL_HATS) {
                if (!Hats.availableHats.contains(hat)) {
                    int x = (50 + gx) * col;
                    int y = (50 + gy) * row;

                    scroll.draw(new CosmeticPreview(hat == player.hat, Textures.getHatPreview(hat), () -> {
                        long now = System.currentTimeMillis();
                        if (now - lastPurchaseTime >= 3000) {
                            Utils.notify(NotificationKind.Info, "Purchase processing", "Please wait 3 seconds");
                            Auth.buyHat(hat);
                            lastPurchaseTime = now;
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            SaturnClient.client.setScreen(new HatStore(now));
                        } else {
                            Utils.notify(NotificationKind.Error, "Timeout error", "Please wait 3 seconds");
                        }
                    }).dimensions(50, 50).position(x, y));

                    String t = "50";

                    scroll.draw(new Text(t).position(x + Fonts.centerX(50, t, Text.font.value), y + 53).scale(0.5f));

                    scroll.draw(new ImageTexture(Textures.COINS).dimensions(16, 16)
                            .position(x + Fonts.getWidth(t, Text.font.value) + 4, y + 52).scale(0.5f));

                    if (col == 8) {
                        col = 0;
                        row++;
                    } else {
                        col++;
                    }
                }
            }
        }

        int scrollWidth = 480 + 40 + (gx * 2) + (p * 2);

        draw(scroll.dimensions(scrollWidth, 350).centerOffset(width, height, 15, 0));

        draw(new Sidebar(5, this::close).centerOffset(width, height, -((scrollWidth - 30) / 2 + 20), 0));

        draw(new TabMenu(1,
                new TabMenuComponent(Textures.CLOAK, () -> {
                    SaturnClient.client.setScreen(new CloakStore());
                }),
                new TabMenuComponent(Textures.HAT, () -> {
                })).centerOffset(width, height, 0, -195));
    }
}
