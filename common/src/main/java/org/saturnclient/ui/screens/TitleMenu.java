package org.saturnclient.ui.screens;

import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.config.AnimationConfig;
import org.saturnclient.config.Config;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.anim.Fade;
import org.saturnclient.ui.anim.SlideFade;
import org.saturnclient.ui.anim.SlideY;
import org.saturnclient.ui.elements.AnimationStagger;
import org.saturnclient.ui.elements.Button;
import org.saturnclient.ui.elements.ImageTexture;
import org.saturnclient.ui.elements.TextureButton;
import org.saturnclient.ui.resources.Fonts;
import org.saturnclient.ui.resources.Textures;
import org.saturnclient.ui.screens.cosmetics.CloakMenu;

public class TitleMenu extends SaturnScreen {
    public TitleMenu() {
        super("Title Menu");
    }

    @Override
    public void ui() {
        backgroundBlur = 0;

        draw(new ImageTexture(Textures.LOGO_TEXT).dimensions(98, 10).centerOffset(width, height, 0, -40)
                .animation(new Fade(AnimationConfig.logo.duration.value)));
        draw(new ImageTexture(Config.getLogo()).dimensions(98, 98).centerOffset(width, height, 0, -84)
                .animation(new SlideY(AnimationConfig.logo, -20)));

        AnimationStagger mainButtonStagger = new AnimationStagger(AnimationConfig.mainMenu);

        // TODO implement all of these

        mainButtonStagger.draw(new Button("SINGLEPLAYER", () -> {
            // MinecraftProvider.PROVIDER.setScreen(new SelectWorldScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).position(0, 0)
                .animation(new SlideFade(AnimationConfig.mainMenu, -10)));

        mainButtonStagger.draw(new Button("MULTIPLAYER", () -> {
            // MinecraftProvider.PROVIDER.setScreen(new MultiplayerScreen(new TitleMenu()));
        }).scale(0.69f).dimensions(420, 52).position(0, 40)
                .animation(new SlideFade(AnimationConfig.mainMenu, -10)));

        mainButtonStagger.draw(new Button("OPTIONS", () -> {
            // MinecraftProvider.PROVIDER.setScreen(new OptionsScreen(new TitleMenu(), SaturnClient.client.options));
        }).scale(0.69f).dimensions(206, 52).position(0, 80)
                .animation(new SlideFade(AnimationConfig.mainMenu, -10)));

        mainButtonStagger.draw(new Button("QUIT", () -> {
            // client.scheduleStop();
        }).scale(0.69f).dimensions(206, 52).position(146, 80)
                .animation(new SlideFade(AnimationConfig.mainMenu, -10)));

        draw(mainButtonStagger.dimensions(289, 143).centerOffset(width, height, 0, 50));

        int s = Fonts.getHeight() + 20;

        AnimationStagger saturnStagger = new AnimationStagger(AnimationConfig.mainMenu.stagger.value);

        saturnStagger.draw(new TextureButton(Textures.HUD_ICON, () -> {
            MinecraftProvider.PROVIDER.setScreen(new HudEditor());
        }).dimensions(s, s).position(0, 0).animation(new SlideFade(AnimationConfig.mainMenu, s + 8)));

        saturnStagger.draw(new TextureButton(Textures.SETTINGS, () -> {
            MinecraftProvider.PROVIDER.setScreen(new ModMenu());
        }).dimensions(s, s).position(s + 2, 0)
                .animation(new SlideFade(AnimationConfig.mainMenu, s + 8)));

        saturnStagger.draw(new TextureButton(Textures.CLOAK, () -> {
            MinecraftProvider.PROVIDER.setScreen(new CloakMenu());
        }).dimensions(s, s).position((s + 2) * 2, 0)
                .animation(new SlideFade(AnimationConfig.mainMenu, s + 8)));

        draw(saturnStagger.dimensions(((s + 2) * 2) + s, s).centerHorizontal(width, height, 0, 8));
    }
}
