package org.saturnclient.ui.screens;

import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.config.AnimationConfig;
import org.saturnclient.config.Config;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.anim.Fade;
import org.saturnclient.ui.anim.SlideY;
import org.saturnclient.ui.elements.AnimationStagger;
import org.saturnclient.ui.elements.Button;
import org.saturnclient.ui.elements.ImageTexture;
import org.saturnclient.ui.elements.TextureButton;
import org.saturnclient.ui.resources.Textures;
import org.saturnclient.ui.screens.cosmetics.CloakMenu;

public class ShiftMenu extends SaturnScreen {
    public ShiftMenu() {
        super("Shift Menu");
    }

    @Override
    public void ui() {
        // Logo animations (unchanged)
        draw(new ImageTexture(Textures.LOGO_TEXT)
                .dimensions(98, 10)
                .centerOffset(width, height, 0, -36)
                .animation(new Fade(AnimationConfig.logo.duration.value)));

        draw(new ImageTexture(Config.getLogo())
                .dimensions(98, 98)
                .centerOffset(width, height, 0, -80)
                .animation(new SlideY(AnimationConfig.logo, -20)));

        // Spacing and sizes
        int btnWidth = 120;
        int btnHeight = 40;
        int spacing = 4;

        // Create stagger

        AnimationStagger stagger = new AnimationStagger(AnimationConfig.shiftMenu.stagger.value);

        stagger.draw(new TextureButton(Textures.HUD_ICON, () -> {
            MinecraftProvider.PROVIDER.setScreen(new HudEditor());
        }).dimensions(btnHeight, btnHeight).position(0, 0)
                .animation(new Fade(AnimationConfig.shiftMenu)));

        stagger.draw(new Button("Settings", () -> {
            MinecraftProvider.PROVIDER.setScreen(new ModMenu());
        }).dimensions(btnWidth, btnHeight).position(btnHeight + spacing, 0)
                .animation(new Fade(AnimationConfig.shiftMenu)));

        stagger.draw(new TextureButton(Textures.COSMETICS, () -> {
            MinecraftProvider.PROVIDER.setScreen(new CloakMenu());
        }).dimensions(btnHeight, btnHeight).position(btnWidth + btnHeight + (spacing * 2), 0)
                .animation(new Fade(AnimationConfig.shiftMenu)));

        // Draw the stagger at the absolute center of the screen
        draw(stagger.dimensions(btnWidth + (btnHeight * 2) + (spacing * 2), btnHeight).centerOffset(width, height, 0,
                0));
    }
}
