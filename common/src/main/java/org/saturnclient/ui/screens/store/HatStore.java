package org.saturnclient.ui.screens.store;

// import org.saturnclient.cosmetics.hat.Hats;
import org.saturnclient.client.ServiceClient;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.config.AnimationConfig;
import org.saturnclient.config.Theme;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.anim.Fade;
import org.saturnclient.ui.anim.SlideY;
import org.saturnclient.ui.components.CosmeticPreview;
import org.saturnclient.ui.components.Sidebar;
import org.saturnclient.ui.elements.AnimationStagger;
import org.saturnclient.ui.elements.ImageTexture;
import org.saturnclient.ui.elements.Scroll;
import org.saturnclient.ui.elements.TabMenu;
import org.saturnclient.ui.elements.Text;
import org.saturnclient.ui.elements.TabMenu.TabMenuComponent;
import org.saturnclient.ui.resources.Fonts;
import org.saturnclient.ui.resources.Textures;

// import com.mojang.blaze3d.systems.RenderSystem;

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
            // for (String hat : Hats.ALL_HATS) {
            //     if (!Hats.availableHats.contains(hat)) {

            //         int x = (50 + gx) * col;
            //         int y = (50 + gy) * row;

            //         stagger.draw(
            //                 new CosmeticPreview(
            //                         hat == player.hat,
            //                         Textures.getHatPreview(hat),
            //                         () -> handlePurchase(hat))
            //                         .dimensions(50, 50)
            //                         .position(x, y)
            //                         .animation(new SlideY(AnimationConfig.cosmeticsMenu, 16)));

            //         String t = "50";

            //         stagger.draw(
            //                 new Text(t)
            //                         .position(x + Fonts.centerX(50, t, Theme.FONT.value), y + 53)
            //                         .scale(0.5f)
            //                         .animation(new Fade(400)));

            //         stagger.draw(
            //                 new ImageTexture(Textures.COINS)
            //                         .dimensions(16, 16)
            //                         .position(x + Fonts.getWidth(t, Theme.FONT.value) + 4, y + 52)
            //                         .scale(0.5f)
            //                         .animation(new Fade(400)));

            //         if (col == 8) {
            //             col = 0;
            //             row++;
            //         } else {
            //             col++;
            //         }
            //     }
            // }
        }

        scroll.draw(stagger);

        int scrollWidth = 480 + 40 + (gx * 2) + (p * 2);

        draw(scroll
                .dimensions(scrollWidth, 350)
                .centerOffset(width, height, 15, 0));

        draw(new Sidebar(5, this.provider::close)
                .centerOffset(width, height, -((scrollWidth - 30) / 2 + 20), 0)
                .animation(new Fade(400)));

        draw(new TabMenu(1,
                new TabMenuComponent(Textures.CLOAK, () -> {
                    MinecraftProvider.PROVIDER.setScreen(new CloakStore());
                }),
                new TabMenuComponent(Textures.HAT, () -> {
                }))
                .centerOffset(width, height, 0, -195));
    }

    private void handlePurchase(String hat) {
        long now = System.currentTimeMillis();

        if (now - lastPurchaseTime >= 3000) {
            // Utils.notify(NotificationKind.Info, "Purchase processing", "Please wait");

            ServiceClient.buyHat(hat);
            lastPurchaseTime = now;

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }

                // RenderSystem.recordRenderCall(() -> {
                //     MinecraftProvider.PROVIDER.setScreen(new HatStore(now));
                //     // Utils.notify(NotificationKind.Success,
                //     // "Purchase complete",
                //     // "Congrats, enjoy your new hat!");
                // });
            }).start();

        } else {
            // Utils.notify(NotificationKind.Error,
            // "Timeout error",
            // "Please wait 3 seconds");
        }
    }
}
