package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideY;
import org.saturnclient.ui2.elements.Button;
import org.saturnclient.ui2.elements.ImageTexture;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public class TitleMenu extends SaturnScreen {
    public TitleMenu() {
        super("Tile Menu");
    }

    @Override
    public void ui() {
        draw(new ImageTexture(Textures.LOGO_TEXT).dimensions(98, 16).centerOffset(width, height, 0, -40).animation(new Fade(700)));
        draw(new ImageTexture(SaturnClientConfig.getLogo()).dimensions(98, 98).centerOffset(width, height, 0, -82).animation(new SlideY(700, -40)));

        draw(new Button("SINGLEPLAYER", () -> {
            client.setScreen(new SelectWorldScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).centerOffset(width, height, 0, 0));

        draw(new Button("MULTIPLAYER", () -> {
            client.setScreen(new MultiplayerScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).centerOffset(width, height, 0, 40));

        draw(new Button("OPTIONS", () -> {
            client.setScreen(new MultiplayerScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(206, 52).centerOffset(width, height, -73, 80));

        draw(new Button("QUIT", () -> {
            client.scheduleStop();
        }).scale(0.69f).dimensions(206, 52).centerOffset(width, height, 73, 80));
    }
}
