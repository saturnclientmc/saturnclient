package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.menus.CloaksMenu;
import org.saturnclient.saturnclient.menus.HudEditor;
import org.saturnclient.saturnclient.menus.ModMenu;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.SaturnScreen;
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
        draw(new ImageTexture(Textures.LOGO).dimensions(98, 98).centerOffset(width, height, 0, -80).animation(new SlideY(700, -40)));
        draw(new ImageTexture(Textures.LOGO_TEXT).dimensions(98, 16).centerOffset(width, height, 0, -36).animation(new Fade(700)));

        Element button = new Button("Settings", () -> {
            SaturnClient.client.setScreen(new ModMenu());
        }).center(width, height);

        draw(button);

        draw(new TextureButton(Textures.COSMETICS, () -> {
            client.setScreen(new CloaksMenu());
        }).dimensions(button.height, button.height).centerOffset(width, height, (button.width / 2) + (button.height / 2) + 4, 0));

        draw(new TextureButton(Textures.HUD_ICON, () -> {
            client.setScreen(new HudEditor());
        }).dimensions(button.height, button.height).centerOffset(width, height, -((button.width / 2) + (button.height / 2) + 4), 0));
    }
}
