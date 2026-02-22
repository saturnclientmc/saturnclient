package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.screens.cosmetics.CloakMenu;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideFade;
import org.saturnclient.ui2.anim.SlideY;
import org.saturnclient.ui2.elements.AnimationStagger;
import org.saturnclient.ui2.elements.Button;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.TextureButton;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public class TitleMenu extends SaturnScreen {
    public TitleMenu() {
        super("Title Menu");
    }

    @Override
    public void ui() {
        backgroundBlur = 0;

        draw(new ImageTexture(Textures.LOGO_TEXT).dimensions(98, 10).centerOffset(width, height, 0, -36)
                .animation(new Fade(700)));
        draw(new ImageTexture(SaturnClientConfig.getLogo()).dimensions(98, 98).centerOffset(width, height, 0, -82)
                .animation(new SlideY(700, -20)));

        AnimationStagger mainButtonStagger = new AnimationStagger(120);

        mainButtonStagger.draw(new Button("SINGLEPLAYER", () -> {
            client.setScreen(new SelectWorldScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).position(0, 0).animation(new SlideFade(1000, -10)));

        mainButtonStagger.draw(new Button("MULTIPLAYER", () -> {
            client.setScreen(new MultiplayerScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).position(0, 40).animation(new SlideFade(1000, -10)));

        mainButtonStagger.draw(new Button("OPTIONS", () -> {
            client.setScreen(new OptionsScreen(new TitleMenu(), SaturnClient.client.options));
        }).scale(0.69f).dimensions(206, 52).position(0, 80).animation(new SlideFade(1000, -10)));

        mainButtonStagger.draw(new Button("QUIT", () -> {
            client.scheduleStop();
        }).scale(0.69f).dimensions(206, 52).position(146, 80).animation(new SlideFade(1000, -10)));

        draw(mainButtonStagger.dimensions(289, 143).centerOffset(width, height, 0, 60));

        int s = Fonts.getHeight() + 20;

        AnimationStagger saturnStagger = new AnimationStagger(180);

        saturnStagger.draw(new TextureButton(Textures.HUD_ICON, () -> {
            client.setScreen(new HudEditor());
        }).dimensions(s, s).position(0, 0).animation(new SlideFade(1000, s + 8)));

        saturnStagger.draw(new TextureButton(Textures.SETTINGS, () -> {
            SaturnClient.client.setScreen(new ModMenu());
        }).dimensions(s, s).position(s + 2, 0).animation(new SlideFade(1000, s + 8)));

        saturnStagger.draw(new TextureButton(Textures.CLOAK, () -> {
            client.setScreen(new CloakMenu());
        }).dimensions(s, s).position((s + 2) * 2, 0).animation(new SlideFade(1000, s + 8)));

        draw(saturnStagger.dimensions(((s + 2) * 2) + s, s).centerHorizontal(width, height, 0, 8));
    }
}
