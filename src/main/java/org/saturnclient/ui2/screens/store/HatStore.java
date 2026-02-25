package org.saturnclient.ui2.screens.store;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.client.ServiceClient;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideY;
import org.saturnclient.ui2.components.CosmeticPreview;
import org.saturnclient.ui2.components.Sidebar;
import org.saturnclient.ui2.elements.AnimationStagger;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.Notification.NotificationKind;
import org.saturnclient.ui2.elements.Scroll;
import org.saturnclient.ui2.elements.TabMenu;
import org.saturnclient.ui2.elements.TabMenu.TabMenuComponent;
import org.saturnclient.ui2.elements.Text;
import org.saturnclient.ui2.resources.Fonts;
import org.saturnclient.ui2.resources.Textures;

import com.mojang.blaze3d.systems.RenderSystem;

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

        SaturnPlayer player = SaturnPlayer.get();

        AnimationStagger stagger = new AnimationStagger(AnimationConfig.cosmeticsMenu);

        if (player != null) {
            for (String hat : Hats.ALL_HATS) {
                if (!Hats.availableHats.contains(hat)) {

                    int x = (50 + gx) * col;
                    int y = (50 + gy) * row;

                    stagger.draw(
                            new CosmeticPreview(
                                    hat == player.hat,
                                    Textures.getHatPreview(hat),
                                    () -> handlePurchase(hat))
                                    .dimensions(50, 50)
                                    .position(x, y)
                                    .animation(new SlideY(AnimationConfig.cosmeticsMenu, 16)));

                    String t = "50";

                    stagger.draw(
                            new Text(t)
                                    .position(x + Fonts.centerX(50, t, Theme.FONT.value), y + 53)
                                    .scale(0.5f)
                                    .animation(new Fade(400)));

                    stagger.draw(
                            new ImageTexture(Textures.COINS)
                                    .dimensions(16, 16)
                                    .position(x + Fonts.getWidth(t, Theme.FONT.value) + 4, y + 52)
                                    .scale(0.5f)
                                    .animation(new Fade(400)));

                    if (col == 8) {
                        col = 0;
                        row++;
                    } else {
                        col++;
                    }
                }
            }
        }

        scroll.draw(stagger);

        int scrollWidth = 480 + 40 + (gx * 2) + (p * 2);

        draw(scroll
                .dimensions(scrollWidth, 350)
                .centerOffset(width, height, 15, 0));

        draw(new Sidebar(5, this::close)
                .centerOffset(width, height, -((scrollWidth - 30) / 2 + 20), 0)
                .animation(new Fade(400)));

        draw(new TabMenu(1,
                new TabMenuComponent(Textures.CLOAK, () -> {
                    SaturnClient.client.setScreen(new CloakStore());
                }),
                new TabMenuComponent(Textures.HAT, () -> {
                }))
                .centerOffset(width, height, 0, -195));
    }

    private void handlePurchase(String hat) {
        long now = System.currentTimeMillis();

        if (now - lastPurchaseTime >= 3000) {
            Utils.notify(NotificationKind.Info, "Purchase processing", "Please wait");

            ServiceClient.buyHat(hat);
            lastPurchaseTime = now;

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                RenderSystem.recordRenderCall(() -> {
                    SaturnClient.client.setScreen(new HatStore(now));
                    Utils.notify(NotificationKind.Success,
                            "Purchase complete",
                            "Congrats, enjoy your new hat!");
                });
            }).start();

        } else {
            Utils.notify(NotificationKind.Error,
                    "Timeout error",
                    "Please wait 3 seconds");
        }
    }
}
