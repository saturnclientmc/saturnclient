package org.saturnclient.saturnclient.menus;

import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.FadeIn;
import org.saturnclient.ui.animations.Slide;
import org.saturnclient.ui.widgets.SaturnButton;
import org.saturnclient.ui.widgets.SaturnImage;
import org.saturnclient.ui2.screens.ShiftMenu;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

public class SaturnTitleScreen extends SaturnUi {
    public SaturnTitleScreen() {
        super(Text.literal("Title Screen"));
    }

    @Override
    protected void init() {
        blur = false;

        int logoSize = 49;
        int bX = (width - 150) / 2;
        int bY = height / 2 - logoSize - 27;

        draw(
                new SaturnImage(SaturnClientConfig.getLogo())
                        .setX((width - logoSize) / 2)
                        .setY(bY)
                        .setWidth(logoSize)
                        .setHeight(logoSize)
                        .setAnimations(new Slide(3, 14)));

        bY += 44;

        draw(
                new SaturnImage(Textures.LOGO_TEXT)
                        .setX((width - logoSize) / 2)
                        .setY(bY)
                        .setWidth(logoSize)
                        .setHeight(8)
                        .setAnimations(new FadeIn(3)));

        bY += 15;

        draw(new SaturnButton("SINGLEPLAYER", () -> {
            client.setScreen(new SelectWorldScreen(new SaturnTitleScreen()));
        }).setBold(true).setWidth(210).setHeight(26).setX(bX).setY(bY).setScale(0.69f));

        bY += 20;

        draw(new SaturnButton("MULTIPLAYER", () -> {
            client.setScreen(new MultiplayerScreen(new SaturnTitleScreen()));
        }).setBold(true).setWidth(210).setHeight(26).setX(bX).setY(bY).setScale(0.69f));

        bY += 20;

        draw(new SaturnButton("OPTIONS", () -> {
            client.setScreen(new OptionsScreen(new SaturnTitleScreen(), this.client.options));
        }).setBold(true).setWidth(103).setHeight(26).setX(bX).setY(bY).setScale(0.69f));

        draw(new SaturnButton("QUIT", () -> {
            client.scheduleStop();
        }).setBold(true).setWidth(103).setHeight(26).setX(bX + 74).setY(bY).setScale(0.69f));

        bY += 20;

        draw(new SaturnButton("SATURN OPTIONS", () -> {
            client.setScreen(new ShiftMenu());
        }).setBold(true).setWidth(210).setHeight(26).setX(bX).setY(bY).setScale(0.69f));

        super.init();
    }
}
