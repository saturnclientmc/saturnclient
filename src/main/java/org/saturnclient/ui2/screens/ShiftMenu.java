package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.screens.cosmetics.CloakMenu;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.elements.AnimationStagger;
import org.saturnclient.ui2.elements.Button;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.TextureButton;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideY;

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
                .animation(new Fade(700)));

        draw(new ImageTexture(SaturnClientConfig.getLogo())
                .dimensions(98, 98)
                .centerOffset(width, height, 0, -80)
                .animation(new SlideY(700, -20)));

        // Spacing and sizes
        int btnWidth = 120;
        int btnHeight = 40;
        int spacing = 4;

        // Create stagger
        AnimationStagger stagger = new AnimationStagger(50);

        stagger.draw(new TextureButton(Textures.HUD_ICON, () -> {
            client.setScreen(new HudEditor());
        }).dimensions(btnHeight, btnHeight).position(0, 0)
                .animation(new Fade(300)));

        stagger.draw(new Button("Settings", () -> {
            SaturnClient.client.setScreen(new ModMenu());
        }).dimensions(btnWidth, btnHeight).position(btnHeight + spacing, 0)
                .animation(new Fade(300)));

        stagger.draw(new TextureButton(Textures.COSMETICS, () -> {
            client.setScreen(new CloakMenu());
        }).dimensions(btnHeight, btnHeight).position(btnWidth + btnHeight + (spacing * 2), 0)
                .animation(new Fade(300)));

        // Draw the stagger at the absolute center of the screen
        draw(stagger.dimensions(btnWidth + (btnHeight * 2) + (spacing * 2), btnHeight).centerOffset(width, height, 0,
                0));
    }
}
